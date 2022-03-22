package com.cs.config.strategy.plugin.usecase.productidentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetProductIdentifiersForClassifiersForMigration extends AbstractOrientPlugin{

  public GetProductIdentifiersForClassifiersForMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProductIdentifiersForClassifiersForMigration/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<Long,List<Long>> productIdentifiers =  new HashMap<>();
    Set<Long> mustIdentifiers = new HashSet<Long>();
    Set<Long> shouldIdentifiers = new HashSet<Long>();
    Set<Long> removedMandatoryIdentifiers = new HashSet<Long>();
    Set<Long> translatablePropertyIIDs = new HashSet<Long>();
    Map<String, Object> referencedElements = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    
    List<Long> classifiers = (List<Long>) requestMap.get("classifiers");
    fillIdentifiers(productIdentifiers, mustIdentifiers, shouldIdentifiers, translatablePropertyIIDs, classifiers, referencedElements,
        referencedAttributes, referencedTags);
    shouldIdentifiers.removeAll(mustIdentifiers);
    List<Long> removedClassifiers = (List<Long>) requestMap.get("removedClassifiers");
    fillIdentifiers(productIdentifiers, removedMandatoryIdentifiers, removedMandatoryIdentifiers, new HashSet<Long>(), removedClassifiers,
        new HashMap<String, Object>(), new HashMap<String, Object>(), new HashMap<String, Object>());
    removedMandatoryIdentifiers.removeIf(n -> (mustIdentifiers.contains(n) || shouldIdentifiers.contains(n)));
    
    Map<String, Object> hashMap = new HashMap<>();
    hashMap.put(IDataRulesHelperModel.MUST, mustIdentifiers);
    hashMap.put(IDataRulesHelperModel.SHOULD, shouldIdentifiers);
    hashMap.put(IDataRulesHelperModel.PRODUCT_IDENTIFIERS, productIdentifiers);
    hashMap.put(IDataRulesHelperModel.REMOVED_MANDATORY_IDENTIFIERS, removedMandatoryIdentifiers);
    hashMap.put(IDataRulesHelperModel.TRANSLATABLE_PROPERTY_IIDS, translatablePropertyIIDs);
    hashMap.put(IDataRulesHelperModel.REFERENCED_ELEMENTS, referencedElements);
    hashMap.put(IDataRulesHelperModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    hashMap.put(IDataRulesHelperModel.REFERENCED_TAGS, referencedTags);
    return hashMap;
  }

  private void fillIdentifiers(Map<Long, List<Long>> productIdentifiers, Set<Long> mustIdentifiers, Set<Long> shouldIdentifiers,
      Set<Long> translatablePropertyIIDs, List<Long> classifiers, Map<String, Object> referencedElements,
      Map<String, Object> referencedAttributes, Map<String, Object> referencedTags) throws Exception, NotFoundException
  {
    if(classifiers == null || classifiers.isEmpty()) {
      return;
    }
    for (Object classifier : classifiers) {
      Long classifieriid;
      if(classifier instanceof Integer) {
        classifieriid = Long.valueOf((Integer)classifier);
      }
      else {
        classifieriid = (Long) classifier;
      }
      List<Long> propertyiids =  new ArrayList<>();
      Vertex classifierNode =  null;
      try {
        classifierNode = UtilClass.getClassifierByIID(classifieriid, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch(NotFoundException e) {
        classifierNode = UtilClass.getClassifierByIID(classifieriid, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      Iterable<Vertex> kpNodes = classifierNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      String classifierCode = UtilClass.getCode(classifierNode);
      for(Vertex kpNode : kpNodes) {
        String type = kpNode.getProperty(IReferencedSectionAttributeModel.TYPE);
        if(!type.equals(CommonConstants.ATTRIBUTE) && !type.equals(CommonConstants.TAG)) {
          continue;
        }
        Boolean isIdentifier = kpNode.getProperty(IReferencedSectionAttributeModel.IS_IDENTIFIER);
        Iterator<Vertex> propertyIterator = kpNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY).iterator();
        if(!propertyIterator.hasNext()) {
        	throw new NotFoundException();
        }
        Vertex propertyNode = propertyIterator.next();
        Long propertyiid = Long.parseLong(propertyNode.getProperty(IAttribute.PROPERTY_IID).toString());
        if(type.equals(CommonConstants.ATTRIBUTE)) {
          if(isIdentifier) {
            propertyiids.add(propertyiid);
          }
          if((Boolean) propertyNode.getProperty(IAttribute.IS_TRANSLATABLE)) {
            translatablePropertyIIDs.add(propertyiid);
          }
        }
		if(!mustIdentifiers.contains(propertyiid) && (Boolean)kpNode.getProperty(ISectionAttribute.IS_MANDATORY)) {
        	mustIdentifiers.add(propertyiid);
        }else
        if(!shouldIdentifiers.contains(propertyiid) && (Boolean)kpNode.getProperty(ISectionAttribute.IS_SHOULD)) {
        	shouldIdentifiers.add(propertyiid);
        }
		  fillReferencedElemnetsData(kpNode, classifierCode, referencedElements, referencedAttributes, referencedTags);
      }
      if(!propertyiids.isEmpty()) {
        Object property2 = classifierNode.getProperty(IKlass.CLASSIFIER_IID);
        
        if(property2 instanceof Integer) {
          productIdentifiers.put(Long.valueOf((Integer)property2), propertyiids);
        }else {
          productIdentifiers.put((Long)property2, propertyiids);
        }
      }
    }
  }
  
  private void fillReferencedElemnetsData(Vertex klassPropertyNode, String klassId, Map<String, Object> referencedElements,
      Map<String, Object> referencedAttributes, Map<String, Object> referencedTags) throws Exception
  {
    String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
    String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
    Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
    referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
    referencedElementMap.put(CommonConstants.CODE_PROPERTY, entityId);
    
    if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
      List<Map<String, Object>> defaultTagValues = KlassUtils.getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
      referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
      List<String> selectedTagValues = KlassUtils.getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
      referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
      
      Vertex entityVertex = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = TagUtils.getTagMap(entityVertex, true);
      
      Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
      if (isVersionable == null) {
        referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
      }
      
      String tagType = (String) referencedElementMap.get(CommonConstants.TAG_TYPE_PROPERTY);
      if (tagType != null && !tagType.equals("")) {
        entity.put(ITag.TAG_TYPE, tagType);
      }
      else {
        referencedElementMap.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
      }
      
      Boolean isMultiselect = (Boolean) referencedElementMap.get(ISectionTag.IS_MULTI_SELECT);
      if (isMultiselect != null) {
        entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
      }
      else {
        referencedElementMap.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
      }
      List<String> selectedTagValuesList = (List<String>) referencedElementMap.remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
      filterChildrenTagsInKlass(entity, selectedTagValuesList, (Map<String, Object>) referencedTags.get(entityId));
      referencedTags.put(entityId, entity);
    }
    
    if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
      Vertex entityVertex = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      Map<String, Object> entity = AttributeUtils.getAttributeMap(entityVertex);
      String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
      if (defaultValue == null || defaultValue.equals("")) {
        referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE, entity.get(IAttribute.DEFAULT_VALUE));
      }
      Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionAttribute.IS_VERSIONABLE);
      if (isVersionable == null) {
        referencedElementMap.put(ISectionAttribute.IS_VERSIONABLE, entity.get(IAttribute.IS_VERSIONABLE));
      }
      String valueAsHtml = (String) referencedElementMap.get(ISectionAttribute.VALUE_AS_HTML);
      if (valueAsHtml == null || valueAsHtml.equals("")) {
        referencedElementMap.put(ISectionAttribute.VALUE_AS_HTML, entity.get(IAttribute.VALUE_AS_HTML));
      }
      if (entity.get(IAttribute.TYPE).equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
          || entity.get(IAttribute.TYPE).equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
        AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags, entity);
      }
      if (!referencedAttributes.containsKey(entityId)) {
        referencedAttributes.put(entityId, entity);
      }
    }
    if (referencedElements.containsKey(entityId)) {
      Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements.get(entityId);
      mergeReferencedElement(referencedElementMap, existingReferencedElement, klassId, entityType, true);
    }
    else {
      referencedElements.put(entityId, referencedElementMap);
    }
  }
  
  protected void mergeReferencedElement(Map<String, Object> referencedElement, Map<String, Object> existingReferencedElement,
      String klassId, String entityType, Boolean shouldMergeTagValuesList)
  {
    Boolean isVariantAllowed = (Boolean) referencedElement.get(IReferencedSectionElementModel.IS_VARIANT_ALLOWED);
    if (isVariantAllowed != null && isVariantAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_VARIANT_ALLOWED, isVariantAllowed);
    }
    
    Integer existingNumberOfVersionsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    Integer numberOfVersionsAllowed = (Integer) referencedElement.get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    if (numberOfVersionsAllowed != null && existingNumberOfVersionsAllowed != null
        && numberOfVersionsAllowed > existingNumberOfVersionsAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED, numberOfVersionsAllowed);
    }
    
    Boolean existingIsSkipped = (Boolean) existingReferencedElement.get(IReferencedSectionElementModel.IS_SKIPPED);
    Boolean newIsSkipped = (Boolean) referencedElement.get(IReferencedSectionElementModel.IS_SKIPPED);
    if (newIsSkipped != null && existingIsSkipped != null && (!existingIsSkipped || !newIsSkipped)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, false);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, true);
    }
    
    if (entityType.equals(CommonConstants.TAG_PROPERTY)) {
      Boolean existingIsMultiselect = (Boolean) existingReferencedElement.get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      Boolean newIsMultiselect = (Boolean) referencedElement.get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      Boolean isMultiselect = mergeConflictingBooleanValues(existingIsMultiselect, newIsMultiselect);
      existingReferencedElement.put(IReferencedSectionTagModel.IS_MULTI_SELECT, isMultiselect);
      
      if (shouldMergeTagValuesList) {
        //TODO: Uncomment the below part, when refAttributes and refTags are filled after refElements are filled for all types, and use this selectedtagvalueslist there to update refTag map.
        // Code for Merging the List for Allowed Values
        /*  List<String> existingSelectedTagValues = (List<String>) existingReferencedElement.get(CommonConstants.SELECTED_TAG_VALUES_LIST);
        List<String> newSelectedTagValues = (List<String>) referencedElement.remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        if (newSelectedTagValues.isEmpty()) {
          existingSelectedTagValues.clear();
        }
        else if (!existingSelectedTagValues.isEmpty()) {
          newSelectedTagValues.forEach(tagValue -> {
            if (!existingSelectedTagValues.contains(tagValue)) {
              existingSelectedTagValues.add(tagValue);
            }
          });
        }*/
      }
    }
    else if (entityType.equals(CommonConstants.ATTRIBUTE)) {
      String attribueVariantContextId = (String) referencedElement.get(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT);
      if (existingReferencedElement.get(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT) == null
          && attribueVariantContextId != null) {
        existingReferencedElement.put(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT, attribueVariantContextId);
      }
    }
    
    Integer existingNumberOfItemsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    Integer numberOfItemsAllowed = (Integer) referencedElement.get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    if (numberOfItemsAllowed != null && existingNumberOfItemsAllowed != null && numberOfItemsAllowed > existingNumberOfItemsAllowed) {
      existingReferencedElement.put(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED, numberOfItemsAllowed);
    }
    
    Boolean existingIsVersionable = (Boolean) existingReferencedElement.get(IAttribute.IS_VERSIONABLE);
    Boolean isVersionable = (Boolean) referencedElement.get(IAttribute.IS_VERSIONABLE);
    if ((isVersionable != null) && (existingIsVersionable != null) && (existingIsVersionable || isVersionable)) {
      existingReferencedElement.put(IAttribute.IS_VERSIONABLE, true);
    }
  }
  
  protected void filterChildrenTagsInKlass(Map<String, Object> newTagMap, List<String> selectedTagValuesList,
      Map<String, Object> existingTagMap)
  {
    if (selectedTagValuesList.size() == 0) {
      return;
    }
    
    List<Map<String, Object>> newChildren = (List<Map<String, Object>>) newTagMap.get(ITag.CHILDREN);
    
    if (existingTagMap == null) {
      List<Map<String, Object>> childTagsToRemove = new ArrayList<>();
      for (Map<String, Object> subTag : newChildren) {
        if (!selectedTagValuesList.contains((String) subTag.get(ITag.ID))) {
          childTagsToRemove.add(subTag);
        }
      }
      
      if (childTagsToRemove.size() != 0) {
        newChildren.removeAll(childTagsToRemove);
      }
    }
    else {
      List<Map<String, Object>> existingChildren = (List<Map<String, Object>>) existingTagMap.get(ITag.CHILDREN);
      List<String> existingChildTagIds = new ArrayList<>();
      for (Map<String, Object> tagValue : existingChildren) {
        existingChildTagIds.add((String) tagValue.get(ITagValue.ID));
      }
      selectedTagValuesList.removeAll(existingChildTagIds);
      if (!selectedTagValuesList.isEmpty()) {
        List<Map<String, Object>> childTagsToAdd = new ArrayList<>();
        for (Map<String, Object> subTag : newChildren) {
          if (selectedTagValuesList.contains((String) subTag.get(ITag.ID))) {
            childTagsToAdd.add(subTag);
          }
        }
        existingChildren.addAll(childTagsToAdd);
      }
      newTagMap.put(ITag.CHILDREN, existingChildren);
    }
  }
  
  protected Boolean mergeConflictingBooleanValues(Boolean existingValue, Boolean newValue)
  {
    if (newValue == null) {
      newValue = false;
    }
    if (existingValue == null) {
      existingValue = false;
    }
    return newValue || existingValue;
  }
}
