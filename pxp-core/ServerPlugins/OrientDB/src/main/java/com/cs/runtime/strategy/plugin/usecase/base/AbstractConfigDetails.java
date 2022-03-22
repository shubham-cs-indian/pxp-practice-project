package com.cs.runtime.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.datarule.IAttributeConflictingValuesModel;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndType;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.repository.language.LanguageRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigDetails extends AbstractOrientPlugin {
  
  public AbstractConfigDetails(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void fillReferencedPropertyCollections(IGetConfigDetailsHelperModel helperModel,
      Vertex propertyCollectionNode, Map<String, Object> mapToReturn, String templateId,
      Set<String> entitiesToFetch)
  {
    Map<String, Object> referencedPropertyCollectionMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionNode);
    if (referencedPropertyCollectionMap.containsKey(propertyCollectionId)) {
      if (templateId != null) {
        Map<String, Object> referencedPropertyCollection = (Map<String, Object>) referencedPropertyCollectionMap
            .get(propertyCollectionId);
        Map<String, Set<String>> templateIdVsAssociatedPropertyIds = helperModel
            .getTemplateIdVsAssociatedPropertyIds();
        List<Map<String, Object>> elements = (List<Map<String, Object>>) referencedPropertyCollection
            .get(IReferencedPropertyCollectionModel.ELEMENTS);
        Set<String> entityIds = new HashSet<>();
        for (Map<String, Object> element : elements) {
          entityIds.add((String) element.get(IReferencedPropertyCollectionElementModel.ID));
        }
        if (templateIdVsAssociatedPropertyIds.containsKey(templateId)) {
          templateIdVsAssociatedPropertyIds.get(templateId)
              .addAll(entityIds);
        }
        else {
          templateIdVsAssociatedPropertyIds.put(templateId, entityIds);
        }
      }
      
      return;
    }
    
    Map<String, Object> referencedPropertyCollection = UtilClass.getMapFromVertex(new ArrayList<>(),
        propertyCollectionNode);
    referencedPropertyCollectionMap.put(propertyCollectionId, referencedPropertyCollection);
    
    List<Map<String, Object>> elementsList = new ArrayList<Map<String, Object>>();
    Iterable<Edge> entityToRelationships = propertyCollectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
    Set<String> entityIds = new HashSet<>();
    for (Edge entityTo : entityToRelationships) {
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      String entityId = UtilClass.getCodeNew(entityNode);
      if (entitiesToFetch != null && entitiesToFetch.size() > 0
          && !entitiesToFetch.contains(entityId)) {
        continue;
      }
      
      Map<String, Object> propertyCollectionElement = new HashMap<String, Object>();
      propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
      elementsList.add(propertyCollectionElement);
      
      entityIds.add(entityId);
    }
    helperModel.getEntityIds()
        .addAll(entityIds);
    if (templateId != null) {
      Map<String, Set<String>> templateIdVsAssociatedPropertyIds = helperModel
          .getTemplateIdVsAssociatedPropertyIds();
      if (templateIdVsAssociatedPropertyIds.containsKey(templateId)) {
        templateIdVsAssociatedPropertyIds.get(templateId)
            .addAll(entityIds);
      }
      else {
        templateIdVsAssociatedPropertyIds.put(templateId, entityIds);
      }
    }
    List<String> propertySequenceIds = propertyCollectionNode.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
    PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds,CommonConstants.ID_PROPERTY);
    
    referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
  }
  
  protected void manageReferencedPermissionsForAdmin(Vertex userInRole,
      Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    referencedPermissions.put(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION,
        GlobalPermissionUtils.getAllRightsGlobalPermission());
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole,
        referencedPermissions);
  }
  
  /**
   * fill referencedElements and all other associated referencedEntity if it is
   * present in entityNodesList and linked to klass or taxonomy Nodes passed as
   * parameter. Also it maintain(fill) all the entityIds added in elementsAdded
   *
   * @author Lokesh
   * @param klassIds
   * @param nodeLabel
   * @param entityIds
   * @param mapToReturn
   * @param contextId
   *          : property context id if not null then it will return the section
   *          elements linked to these context only.
   * @throws Exception
   */
  protected void fillReferencedElementsAndReferencedEntities(Map<String, Object> mapToReturn,
      List<String> klassIds, String nodeLabel, Set<String> entityIds, String contextId,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, nodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      Iterable<Vertex> kPNodesIterable = klassNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassPropertyNode : kPNodesIterable) {
        String klassPropertyContextId = null;
        
        Iterator<Vertex> entityIterator = klassPropertyNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        Vertex entityNode = entityIterator.next();
        String entityId = UtilClass.getCodeNew(entityNode);
        
        Iterator<Vertex> attributeContextsIterator = klassPropertyNode
            .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
            .iterator();
        if (contextId != null && !attributeContextsIterator.hasNext()) {
          continue;
        }
        
        if (attributeContextsIterator.hasNext()) {
          Vertex attributeContext = attributeContextsIterator.next();
          klassPropertyContextId = UtilClass.getCodeNew(attributeContext);
          if (contextId != null && !contextId.equals(klassPropertyContextId)) {
            continue;
          }
          
          Map<String, Set<String>> contextIdVsPropertyIds = helperModel.getContextIdVsPropertyIds();
          Set<String> klassPropertyContextIds = contextIdVsPropertyIds.get(klassPropertyContextId);
          if (klassPropertyContextIds == null) {
            klassPropertyContextIds = new HashSet<>();
            klassPropertyContextIds.add(entityId);
          }
          contextIdVsPropertyIds.put(klassPropertyContextId, klassPropertyContextIds);
        }
        
        if (!entityIds.contains(entityId)) {
          continue;
        }
        String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
        
        Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
        referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
        if (klassPropertyContextId != null) {
          referencedElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT,
              klassPropertyContextId);
        }
        if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
          List<Map<String, Object>> defaultTagValues = KlassUtils
              .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
          List<String> selectedTagValues = KlassUtils
              .getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
        }
        fillReferencedElementInRespectiveMap(entityType, entityNode, mapToReturn,
            referencedElementMap, helperModel);
        
        // if referencedTaxonomy is null no need to add referencedElement of it
        if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAXONOMY)
            && !mapToReturn.containsKey(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES)) {
          continue;
        }
        
        if (referencedElements.containsKey(entityId)) {
          Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
              .get(entityId);
          mergeReferencedElement(referencedElementMap, existingReferencedElement, klassId,
              entityType, true);
        }
        else {
          referencedElements.put(entityId, referencedElementMap);
        }
        referencedElementMap.remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
      }
    }
  }
  
  /**
   * fill referenced Attribute, Tags, roles and relationships in mapToReturn.
   *
   * @author Lokesh
   * @param entityId
   * @param klassId
   * @param entityType
   * @param entityNode
   * @param mapToReturn
   * @param referencedElementMap
   * @param allowedEntities
   * @return
   * @throws Exception
   */
  protected void fillReferencedElementInRespectiveMap(String entityType, Vertex entityNode,
      Map<String, Object> mapToReturn, Map<String, Object> referencedElementMap,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    
    Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionAttribute.IS_VERSIONABLE);
    String entityId = UtilClass.getCodeNew(entityNode);
    Map<String, Object> entity = new HashMap<>();
    switch (entityType) {
      case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
        entity = AttributeUtils.getAttributeMap(entityNode);
        if (entity.get(IAttribute.TYPE)
            .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
            || entity.get(IAttribute.TYPE)
                .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(
              referencedAttributes, referencedTags, entity);
        }
        if (!referencedAttributes.containsKey(entityId)) {
          referencedAttributes.put(entityId, entity);
        }
        
        if (isVersionable == null) {
          referencedElementMap.put(ISectionAttribute.IS_VERSIONABLE,
              entity.get(IAttribute.IS_VERSIONABLE));
        }
        
        String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
        if (defaultValue == null || defaultValue.equals("")) {
          referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE,
              entity.get(IAttribute.DEFAULT_VALUE));
        }
        String valueAsHtml = (String) referencedElementMap.get(ISectionAttribute.VALUE_AS_HTML);
        if (valueAsHtml == null || valueAsHtml.equals("")) {
          referencedElementMap.put(ISectionAttribute.VALUE_AS_HTML,
              entity.get(IAttribute.VALUE_AS_HTML));
        }
        break;
      
      case SystemLevelIds.PROPERTY_TYPE_TAG:
        // Only filter tag values for types mentioned in list below
        List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
            SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
        
        if (helperModel.getShouldUseTagIdTagValueIdsMap()
            && tagTypes.contains(entityNode.getProperty(ITag.TAG_TYPE))) {
          Map<String, List<String>> tagIdTagValueIdsMap = helperModel.getTagIdTagValueIdsMap();
          List<String> tagValueIds = tagIdTagValueIdsMap.get(entityId);
          entity = TagUtils.getTagMapWithSelectTagValues(entityNode, tagValueIds);
        }
        else {
          entity = TagUtils.getTagMap(entityNode, false);
        }
        
        List<String> selectedTagValuesList = (List<String>) referencedElementMap
            .remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        filterChildrenTagsInKlass(entity, selectedTagValuesList,
            (Map<String, Object>) referencedTags.get(entityId));
        
        referencedTags.put(entityId, entity);
        String tagType = (String) referencedElementMap.get(CommonConstants.TAG_TYPE_PROPERTY);
        if (tagType != null && !tagType.equals("")) {
          entity.put(ITag.TAG_TYPE, tagType);
        }
        else {
          referencedElementMap.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
        }
        
        if (isVersionable == null) {
          referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
        }
        
        Boolean isMultiselect = (Boolean) referencedElementMap.get(ISectionTag.IS_MULTI_SELECT);
        if (isMultiselect != null) {
          entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
        }
        else {
          referencedElementMap.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
        }
        break;
      
      case SystemLevelIds.PROPERTY_TYPE_ROLE:
        entity = (Map<String, Object>) referencedRoles.get(entityId);
        entity = RoleUtils.getRoleEntityMap(entityNode);
        referencedRoles.put(entityId, entity);
        break;
      
      case SystemLevelIds.PROPERTY_TYPE_TAXONOMY:
        if (referencedTaxonomyMap == null) {
          break;
        }
        Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
            Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
                IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
                IReferencedArticleTaxonomyModel.CODE),
            entityNode);
        fillTaxonomiesChildrenAndParentData(taxonomyMap, entityNode);
        referencedTaxonomyMap.put(entityId, taxonomyMap);
        break;
      
      default:
        break;
    }
  }
  
  protected void filterChildrenTagsInKlass(Map<String, Object> newTagMap,
      List<String> selectedTagValuesList, Map<String, Object> existingTagMap)
  {
    if ( selectedTagValuesList == null || selectedTagValuesList.size() == 0) {
      return;
    }
    
    List<Map<String, Object>> newChildren = (List<Map<String, Object>>) newTagMap
        .get(ITag.CHILDREN);
    
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
      List<Map<String, Object>> existingChildren = (List<Map<String, Object>>) existingTagMap
          .get(ITag.CHILDREN);
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
  
  protected void mergeReferencedElement(Map<String, Object> referencedElement,
      Map<String, Object> existingReferencedElement, String klassId, String entityType,
      Boolean shouldMergeTagValuesList)
  {
    Boolean isVariantAllowed = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_VARIANT_ALLOWED);
    if (isVariantAllowed != null && isVariantAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_VARIANT_ALLOWED,
          isVariantAllowed);
    }
    
    Integer existingNumberOfVersionsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    Integer numberOfVersionsAllowed = (Integer) referencedElement
        .get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    if (numberOfVersionsAllowed != null && existingNumberOfVersionsAllowed != null
        && numberOfVersionsAllowed > existingNumberOfVersionsAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED,
          numberOfVersionsAllowed);
    }
    
    mergeCouplingTypesAndIsMandatoryFields(existingReferencedElement, referencedElement, klassId);
    
    Boolean existingIsSkipped = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_SKIPPED);
    Boolean newIsSkipped = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_SKIPPED);
    if (newIsSkipped != null && existingIsSkipped != null
        && (!existingIsSkipped || !newIsSkipped)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, false);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, true);
    }
    
    if (entityType.equals(CommonConstants.TAG_PROPERTY)) {
      Boolean existingIsMultiselect = (Boolean) existingReferencedElement
          .get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      Boolean newIsMultiselect = (Boolean) referencedElement
          .get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      Boolean isMultiselect = mergeConflictingBooleanValues(existingIsMultiselect,
          newIsMultiselect);
      existingReferencedElement.put(IReferencedSectionTagModel.IS_MULTI_SELECT, isMultiselect);
      
      if (shouldMergeTagValuesList) {
        // Code for Merging the List for Allowed Values
        List<String> existingSelectedTagValues = (List<String>) existingReferencedElement
            .get(CommonConstants.SELECTED_TAG_VALUES_LIST);
        List<String> newSelectedTagValues = (List<String>) referencedElement
            .remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        if (newSelectedTagValues.isEmpty()) {
          existingSelectedTagValues.clear();
        }
        else if (!existingSelectedTagValues.isEmpty()) {
          newSelectedTagValues.forEach(tagValue -> {
            if (!existingSelectedTagValues.contains(tagValue)) {
              existingSelectedTagValues.add(tagValue);
            }
          });
        }
      }
    }
    else if (entityType.equals(CommonConstants.ATTRIBUTE)) {
      String attribueVariantContextId = (String) referencedElement
          .get(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT);
      if (existingReferencedElement
          .get(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT) == null
          && attribueVariantContextId != null) {
        existingReferencedElement.put(IReferencedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT,
            attribueVariantContextId);
      }
    }
    
    Integer existingNumberOfItemsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    Integer numberOfItemsAllowed = (Integer) referencedElement
        .get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    if (numberOfItemsAllowed != null && existingNumberOfItemsAllowed != null
        && numberOfItemsAllowed > existingNumberOfItemsAllowed) {
      existingReferencedElement.put(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED,
          numberOfItemsAllowed);
    }
    
    Boolean existingIsVersionable = (Boolean) existingReferencedElement
        .get(IAttribute.IS_VERSIONABLE);
    Boolean isVersionable = (Boolean) referencedElement.get(IAttribute.IS_VERSIONABLE);
    if ((isVersionable != null) && (existingIsVersionable != null)
        && (existingIsVersionable || isVersionable)) {
      existingReferencedElement.put(IAttribute.IS_VERSIONABLE, true);
    }
    
    Boolean existingIsIdentifier = (Boolean) existingReferencedElement
                .get(ISectionAttribute.IS_IDENTIFIER);
            Boolean isIdentifier = (Boolean) referencedElement.get(ISectionAttribute.IS_IDENTIFIER);
            if ((isIdentifier != null) && (existingIsIdentifier != null)
                && (existingIsIdentifier || isIdentifier)) {
              existingReferencedElement.put(ISectionAttribute.IS_IDENTIFIER, true);
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
  
  protected void mergeCouplingTypesAndIsMandatoryFields(
      Map<String, Object> existingReferencedElement, Map<String, Object> referencedElement,
      String klassId)
  {
    String existingCouplingType = (String) existingReferencedElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    String newCouplingType = (String) referencedElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    
    Boolean existingIsMandatory = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_MANDATORY);
    Boolean newIsMmandatory = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_MANDATORY);
    if (newIsMmandatory != null && existingIsMandatory != null
        && (existingIsMandatory || newIsMmandatory)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_MANDATORY, true);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_MANDATORY, false);
    }
    
    Boolean existingIsShould = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_SHOULD);
    Boolean newIsShould = (Boolean) referencedElement.get(IReferencedSectionElementModel.IS_SHOULD);
    if (newIsShould != null && existingIsShould != null && (existingIsShould || newIsShould)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SHOULD, true);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SHOULD, false);
    }
    
    if (existingCouplingType != null && newCouplingType != null) {
      if ((newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED))
          || (newCouplingType.equals(CommonConstants.TIGHTLY_COUPLED)
              && !existingCouplingType.equals(CommonConstants.DYNAMIC_COUPLED))) {
        
        mergeCouplingSource(existingReferencedElement, referencedElement, existingCouplingType,
            newCouplingType);
        
        if (!existingCouplingType.equals(newCouplingType)) {
          existingReferencedElement.put(IReferencedSectionAttributeModel.COUPLING_TYPE,
              newCouplingType);
          existingReferencedElement.put(IReferencedSectionAttributeModel.DEFAULT_VALUE,
              referencedElement.get(IReferencedSectionAttributeModel.DEFAULT_VALUE));
        }
      }
    }
  }
  
  private void mergeCouplingSource(Map<String, Object> existingReferencedElement,
      Map<String, Object> referencedElement, String existingCouplingType, String newCouplingType)
  {
    List<Map<String, Object>> confictingSourceList = (List<Map<String, Object>>) existingReferencedElement
        .get(IReferencedSectionElementModel.CONFLICTING_SOURCES);
    if (confictingSourceList == null) {
      // No need to calculate conflicting source (This is null is content grid
      // view && variant table
      // view)
      // currently calculationg only of content open
      return;
    }
    if (!existingCouplingType.equals(newCouplingType)
        && newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
      confictingSourceList.clear();
    }
    confictingSourceList.addAll((List<Map<String, Object>>) referencedElement
        .get(IReferencedSectionElementModel.CONFLICTING_SOURCES));
  }
  
  protected void fillReferencedTagsAndLifeCycleStatusTags(Map<String, Object> mapToReturn,
      Vertex klassVertex) throws Exception
  {
    List<String> referencedLifeCycleStatusTags = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_LIFECYCLE_STATUS_TAGS);
    Map<String, Object> referencedTagMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Iterable<Vertex> linkedLifeCycleStatusTags = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
      String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      if (!referencedLifeCycleStatusTags.contains(id)) {
        referencedLifeCycleStatusTags.add(id);
      }
      Vertex linkedTagNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
      String tagId = (String) referencedTag.get(ITag.ID);
      referencedTagMap.put(tagId, referencedTag);
    }
  }
  
  /**
   * fills referencedKlasses and referencedTaxonomies in mapToReturn &
   * maintain(fill) all klassNodes and TaxonomyNodes in klassNodesList and
   * taxonomyNodesList respectively
   *
   * @author Lokesh
   * @param mapToReturn
   * @param nodeLabel
   * @param klassIds
   * @return natureKlassNode
   * @throws Exception
   */
  protected Vertex fillKlassDetailsAndGetNatureKlassNode(Map<String, Object> mapToReturn,
      String nodeLabel, List<String> klassIds, Map<String, Object> referencedDataRuleMap,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Vertex natureKlassNode = null;
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    Set<Vertex> nonNatureNodes = new HashSet<>();
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, nodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      String natureType = klassVertex.getProperty(IKlass.NATURE_TYPE);
      if (natureType != null && !natureType.isEmpty()) {
        natureKlassNode = klassVertex;
      }
      else {
        nonNatureNodes.add(klassVertex);
      }
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.CODE, IKlass.NATURE_TYPE, IKlass.IS_NATURE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      
      Integer numberOfVersionsToMaintain = (Integer) klassMap
          .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
          .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
        mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
            numberOfVersionsToMaintain);
      }
      
      fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
      fillMandatoryReferencedAttributes(mapToReturn);
      fillDataRulesOfKlass(klassVertex, referencedDataRuleMap, helperModel,
          RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
      
      referencedKlassMap.put(klassId, klassMap);
    }
    helperModel.setNonNatureKlassNodes(nonNatureNodes);
    return natureKlassNode;
  }
  
  protected void fillDataRulesOfKlass(Vertex klassVertex, Map<String, Object> referencedDataRuleMap,
      IGetConfigDetailsHelperModel helperModel, String hasEntityRuleLink) throws Exception
  {
    String query = getDataRulesQuery(klassVertex, helperModel.getOrganizationId(),
        helperModel.getPhysicalCatalogId(), helperModel.getEndpointId(), hasEntityRuleLink);
    Iterable<Vertex> dataRuleVertices = executeQuery(query);
    for (Vertex dataRuleVertex : dataRuleVertices) {
      String dataRuleId = UtilClass.getCodeNew(dataRuleVertex);
      if (referencedDataRuleMap.get(dataRuleId) != null) {
        continue;
      }
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleVertex, true);
      referencedDataRuleMap.put(dataRuleId, dataRuleMap);
    }
  }
  
  /**
   * Description : For all tabs, send mandatory attributes in
   * referencedAttributes even if not present in PC..
   *
   * @author Ajit
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillMandatoryReferencedAttributes(Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_ATTRIBUTES);
    for (String attributeId : IStandardConfig.StandardProperty.MandatoryAttributeCodes) {
      Vertex attributeNode = null;
      try {
        attributeNode = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
      Map<String, Object> attribute = AttributeUtils.getAttributeMap(attributeNode);
      referencedAttributes.put(attributeId, attribute);
    }
  }
  
  /**
   * fill KlassIdsHavingReadPermissions and 'all' if no klasses is there
   *
   * @author Lokesh
   * @param roleNode
   * @param referencedPermissions
   * @param klassIds
   * @throws Exception
   */
  protected void fillKlassIdsHavingReadPermission(Vertex roleNode,
      Map<String, Object> referencedPermissions) throws Exception
  {
    Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    klassIdsHavingReadPermission
        .addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode));
  }
  
  protected String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.CODE, ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        ITaxonomy.BASE_TYPE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  public void mergeCouplingTypeFromOfReferencedElementsFromRelationship(
      Map<String, Object> mapToReturn, List<String> klassIds) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> elementsConflictingValues = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.ELEMENTS_CONFLICTING_VALUES);
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(klassIds, referencedElements,
        elementsConflictingValues);
  }
  
  protected void mergeCouplingTypeFromOfReferencedElementsFromRelationship(List<String> klassIds,
      Map<String, Object> referencedElements, Map<String, Object> elementsConflictingValues)
      throws RelationshipNotFoundException, MultipleLinkFoundException
  {
    if(klassIds.isEmpty() || klassIds == null) {
      return;
    }
    List<Vertex> allAssociatedKRNodes = getAllAssociatedKlassRelationshipsNodes(klassIds);
    for (Vertex klassRelationshipNode : allAssociatedKRNodes) {
      Iterator<Vertex> relationshipNodesIterator = klassRelationshipNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!relationshipNodesIterator.hasNext()) {
        throw new RelationshipNotFoundException();
      }
      Vertex relationshipNode = relationshipNodesIterator.next();
      
      if (relationshipNodesIterator.hasNext()) {
        throw new MultipleLinkFoundException();
      }
      
      Vertex otherSideRelationshipNode = null;
      Iterable<Vertex> klassRelationshipNodes = relationshipNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex klassRelationshipNodeTemp : klassRelationshipNodes) {
        if (!UtilClass.getCodeNew(klassRelationshipNodeTemp)
            .equals(UtilClass.getCodeNew(klassRelationshipNode))) {
          otherSideRelationshipNode = klassRelationshipNodeTemp;
        }
      }
      
      // Below condition is to check self relationship hence continue
      if (otherSideRelationshipNode == null) {
        continue;
      }
      Iterable<Edge> hasRelationshipAttributeEdges = otherSideRelationshipNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE);
      for (Edge hasRelationshipAttributeEdge : hasRelationshipAttributeEdges) {
        Vertex attribute = hasRelationshipAttributeEdge.getVertex(Direction.IN);
        String couplingType = hasRelationshipAttributeEdge
            .getProperty(ISectionElement.COUPLING_TYPE);
        String attributeId = UtilClass.getCodeNew(attribute);
        if (referencedElements.get(attributeId) != null) {
          Map<String, Object> referencedElement = (Map<String, Object>) referencedElements
              .get(attributeId);
          mergeCouplingOfReferencedElementsForRelationships(couplingType, referencedElement,
              UtilClass.getCodeNew(relationshipNode), elementsConflictingValues);
        }
      }
      Iterable<Edge> hasRelationshipTagEdges = otherSideRelationshipNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_RELATIONSHIP_TAG);
      for (Edge hasRelationshipTagEdge : hasRelationshipTagEdges) {
        Vertex tag = hasRelationshipTagEdge.getVertex(Direction.IN);
        String couplingType = hasRelationshipTagEdge.getProperty(ISectionElement.COUPLING_TYPE);
        String tagId = UtilClass.getCodeNew(tag);
        if (referencedElements.get(tagId) != null) {
          Map<String, Object> referencedElement = (Map<String, Object>) referencedElements
              .get(tagId);
          mergeCouplingOfReferencedElementsForRelationships(couplingType, referencedElement,
              UtilClass.getCodeNew(relationshipNode), elementsConflictingValues);
        }
      }
    }
  }
  
  protected List<Vertex> getAllAssociatedKlassRelationshipsNodes(List<String> klassIds)
  {
    List<Vertex> allAssociatedKRNodes = new ArrayList<>();
    String query = "select from " + VertexLabelConstants.KLASS_RELATIONSHIP + " where in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code in "
        + EntityUtil.quoteIt(klassIds);
    Iterable<Vertex> klassRelationshipNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> klassRelationshipNodeIterator = klassRelationshipNodes.iterator();
    List<Vertex> listOfKrNodes = StreamSupport
        .stream(
            Spliterators.spliteratorUnknownSize(klassRelationshipNodeIterator, Spliterator.ORDERED),
            false)
        .collect(Collectors.<Vertex> toList());
    allAssociatedKRNodes.addAll(listOfKrNodes);
    return allAssociatedKRNodes;
  }
  
  private void mergeCouplingOfReferencedElementsForRelationships(String couplingType,
      Map<String, Object> referencedElement, String relationshipId,
      Map<String, Object> elementsConflictingValues)
  {
    if (referencedElement.get(CommonConstants.ATTRIBUTE_VARIANT_CONTEXT) != null) {
      return;
    }
    String currentCouplingType = (String) referencedElement.get(ISectionElement.COUPLING_TYPE);
    String type = (String) referencedElement.get(IReferencedSectionElementModel.TYPE);
    /*
    if (currentCouplingType.equals(CommonConstants.LOOSELY_COUPLED)&& (couplingType.equals(CommonConstants.TIGHTLY_COUPLED) || couplingType.equals(CommonConstants.DYNAMIC_COUPLED))) {
      referencedElement.put(ISectionElement.COUPLING_TYPE, couplingType);
    }
    else if (currentCouplingType.equals(CommonConstants.TIGHTLY_COUPLED) && couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
      referencedElement.put(ISectionElement.COUPLING_TYPE, couplingType);
    }
    */
    Integer couplingNumber = EntityUtil.compareCoupling(currentCouplingType, couplingType);
    if (couplingNumber > 0) {
      referencedElement.put(ISectionElement.COUPLING_TYPE, couplingType);
    }
    
    List<Map<String, Object>> confictingSourceList = (List<Map<String, Object>>) referencedElement
        .get(IReferencedSectionElementModel.CONFLICTING_SOURCES);
    if (confictingSourceList == null) {
      // No need to calculate conflicting source (This is null in case of bulk
      // propogation, etc
      // where conflicting source is of no use)
      // currently calculationg only of content open
      return;
    }
    if (couplingNumber > 0) {
      // couplingType is higher than currentCouplingType
      confictingSourceList.clear();
      
      // clear elementsConflictingValues as new coupling is not comming from
      // klass or taxonomy
      String elementId = (String) referencedElement.get(ISectionElement.ID);
      if (elementsConflictingValues != null && elementsConflictingValues.containsKey(elementId)) {
        ((List<Map<String, Object>>) elementsConflictingValues.get(elementId)).clear();
      }
    }
    
    if (couplingNumber >= 0) {
      // couplingType is higher or equals to currentCouplingType
      Map<String, Object> confictingSourceMap = new HashMap<String, Object>();
      confictingSourceMap.put(IIdAndType.ID, relationshipId);
      confictingSourceMap.put(IIdAndType.TYPE, type);
      confictingSourceMap.put(IElementConflictingValuesModel.SOURCE_TYPE,
          CommonConstants.RELATIONSHIP_CONFLICTING_SOURCE_TYPE);
      confictingSourceMap.put(IAttributeConflictingValuesModel.COUPLING_TYPE, couplingType);
      confictingSourceList.add(confictingSourceMap);
    }
    /*
    if ((couplingType.equals(CommonConstants.DYNAMIC_COUPLED))
        || (couplingType.equals(CommonConstants.TIGHTLY_COUPLED) && !currentCouplingType
            .equals(CommonConstants.DYNAMIC_COUPLED))) {
    
      if (!currentCouplingType.equals(couplingType)
          && couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        confictingSourceList.clear();
    
        //clear elementsConflictingValues as new coupling is not comming from klass or taxonomy
        String elementId = (String) referencedElement.get(ISectionElement.ID);
        if(elementsConflictingValues!=null && elementsConflictingValues.containsKey(elementId))
        {
          ((List<Map<String, Object>>)elementsConflictingValues.get(elementId)).clear();
        }
      }
      Map<String, Object> confictingSourceMap = new HashMap<String, Object>();
      confictingSourceMap.put(IIdAndType.ID, relationshipId);
      confictingSourceMap.put(IIdAndType.TYPE, CommonConstants.RELATIONSHIP);
      confictingSourceList.add(confictingSourceMap);
    }
    */
  }
  
  protected void fillKlassIdsAndTaxonomyIdsHavingReadPermission(Vertex userInRole,
      Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> referencedPermission = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermission
        .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    Set<String> taxonomyIdsHavingRP = (Set<String>) referencedPermission
        .get(IReferencedTemplatePermissionModel.TAXONOMY_IDS_HAVING_RP);
    Set<String> allTaxonomyIdsHavingRP = (Set<String>) referencedPermission
        .get(IReferencedTemplatePermissionModel.ALL_TAXONOMY_IDS_HAVING_RP);
    klassIdsHavingReadPermission
        .addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole));
    GlobalPermissionUtils.fillTaxonomyIdsHavingReadPermission(userInRole, taxonomyIdsHavingRP,
        allTaxonomyIdsHavingRP);
    // following function is called so that allTaxonomyIdsHavingRP contains
    // minor taxonomy as minor
    // taxonomy always has read permission (its visiblity is defined by
    // template)
    fillAllMinorTaxonomyFromReferencedTaxonomies(allTaxonomyIdsHavingRP, configDetails);
  }
  
  /**
   * fill taxonomyIds with referencedTaxonomies Ids if they are minor taxonomy
   * or child of minor taxonomy
   *
   * @param taxonomyIds
   * @param configDetails
   * @throws Exception
   */
  private void fillAllMinorTaxonomyFromReferencedTaxonomies(Set<String> taxonomyIds,
      Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsModel.REFERENCED_TAXONOMIES);
    if (referencedTaxonomies == null || referencedTaxonomies.isEmpty()) {
      return;
    }
    List<String> referencedTaxonomiesIds = new ArrayList<String>(referencedTaxonomies.keySet());
    referencedTaxonomiesIds.removeAll(taxonomyIds);
    for (String taxonomyId : referencedTaxonomiesIds) {
      Vertex taxonomyNode = UtilClass.getVertexById(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      Vertex rootTaxonomy = AttributionTaxonomyUtil.getRootParentTaxonomy(taxonomyNode);
      String taxonomyType = rootTaxonomy.getProperty(ITaxonomy.TAXONOMY_TYPE);
      if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
        taxonomyIds.add(taxonomyId);
      }
    }
  }
  
  protected String getDataRulesQuery(Vertex klass, String organisationId, String physicalCatalogId,
      String endpointId, String hasEntityRuleLink)
  {
    String query = "SELECT FROM (SELECT EXPAND(OUT('" + hasEntityRuleLink + "')) FROM "
        + klass.getId() + " )";
    
    // get rule if
    // 1. rule is connected with the provided organization
    // 2. rule is not link with any organization(i.e it is link with all the
    // organization)
    query = query + " WHERE (( OUT('" + RelationshipLabelConstants.ORGANISATION_RULE_LINK
        + "').code CONTAINS '" + organisationId + "' ) OR OUT('"
        + RelationshipLabelConstants.ORGANISATION_RULE_LINK + "').size() = 0 ) AND ";
    
    // get rule if
    // 1. physicalCatalogIds contains physicalCatalogId
    // 2. physicalCatalogIdy is empty(i.e applicable for all physicalCatalogs)
    query = query + " ( " + IDataRule.PHYSICAL_CATALOG_IDS + " CONTAINS '" + physicalCatalogId
        + "' OR " + IDataRule.PHYSICAL_CATALOG_IDS + ".size() = 0 ) ";
    
    if (physicalCatalogId != null
        && physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      // get rule if
      // 1. rule is connected with the provided endpoint
      // 2. rule is not link with any endpoint(i.e it is link with all the
      // endpoint)
      query = query + " AND (( OUT('" + RelationshipLabelConstants.RULE_ENDPOINT_LINK
          + "').code CONTAINS '" + endpointId + "' ) OR OUT('"
          + RelationshipLabelConstants.RULE_ENDPOINT_LINK + "').size() = 0)";
    }
    
    return query;
  }
  
  protected Iterable<Vertex> executeQuery(String query)
  {
    Iterable<Vertex> dataRuleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return dataRuleVertices;
  }
  
  protected void fillKlassDetails(Map<String, Object> returnMap, List<String> klassIds,
      String endpointId, String organizationId, String physicalCatalogId) throws Exception
  {
    Map<String, Object> klassDataRulesMap = (Map<String, Object>) returnMap
        .get(IConfigDetailsForDataTransferModel.KLASS_DATA_RULES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        fillIdentifierAttributesForType(returnMap, klassVertex);
        
        List<String> dataRuleIds = getDataRulesOfKlass(klassVertex, returnMap, endpointId,
            organizationId, physicalCatalogId);
        klassDataRulesMap.put(klassId, dataRuleIds);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  protected void fillTaxonomiesDetails(Map<String, Object> returnMap, List<String> taxonomyIds,
      String endpointId, String organizationId, String physicalCatalogId) throws Exception
  {
    Map<String, Object> klassDataRulesMap = (Map<String, Object>) returnMap
        .get(IConfigDetailsForDataTransferModel.KLASS_DATA_RULES);
    
    for (String taxonomyId : taxonomyIds) {
      try {
        Vertex taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        fillIdentifierAttributesForType(returnMap, taxonomyVertex);
        
        List<String> dataRuleIds = getDataRulesOfKlass(taxonomyVertex, returnMap, endpointId,
            organizationId, physicalCatalogId);
        klassDataRulesMap.put(taxonomyId, dataRuleIds);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
    }
  }
  
  protected void fillIdentifierAttributesForType(Map<String, Object> mapToReturn, Vertex klassNode)
      throws Exception
  {
    Map<String, Object> typeIdIdentifierAttributeIds = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForDataTransferModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS);
    List<String> identifierAttributesForKlass = new ArrayList<>();
    typeIdIdentifierAttributeIds.put(UtilClass.getCodeNew(klassNode), identifierAttributesForKlass);
    Iterable<Vertex> kPNodesIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropertyNode : kPNodesIterable) {
      Iterator<Vertex> entityIterator = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex entityNode = entityIterator.next();
      String entityId = UtilClass.getCodeNew(entityNode);
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
        Boolean isIdentifierAttribute = klassPropertyNode
            .getProperty(ISectionAttribute.IS_IDENTIFIER);
        if (isIdentifierAttribute != null && isIdentifierAttribute) {
          identifierAttributesForKlass.add(entityId);
        }
      }
    }
  }
  
  protected List<String> getDataRulesOfKlass(Vertex klassVertex, Map<String, Object> returnMap,
      String endpointId, String organizationId, String physicalCatalogId) throws Exception
  {
    Map<String, Object> referencedDataRuleMap = (Map<String, Object>) returnMap
        .get(IConfigDetailsForDataTransferModel.REFERENCED_DATA_RULES);
    List<String> dataRuleIds = new ArrayList<>();
    
    String query = getDataRulesQuery(klassVertex, organizationId, physicalCatalogId, organizationId,
        RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    Iterable<Vertex> dataRuleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex dataRuleVertex : dataRuleVertices) {
      String dataRuleId = UtilClass.getCodeNew(dataRuleVertex);
      dataRuleIds.add(dataRuleId);
      if (referencedDataRuleMap.get(dataRuleId) != null) {
        continue;
      }
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleVertex, true);
      referencedDataRuleMap.put(dataRuleId, dataRuleMap);
    }
    return dataRuleIds;
  }
  
  /*  protected void fillContextualDataTransferProperties(Map<String, Object> mapToReturn,
      List<String> parentTaxonomyIds, List<String> parentKlassIds,
      IGetConfigDetailsHelperModel helperModel, Boolean isValueInheritaceFromParent,
      Boolean isValueInheritaceToChildren)
  {
    Map<String, Object> referencedParentChildContextProperties = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForCustomTabModel.REFERENCED_PARENT_CHILD_CONTEXT_PROPERTIES);
    Set<Vertex> childContentTypeVertices = new HashSet<>();
    Vertex natureNode = helperModel.getNatureNode();
    if (natureNode != null) {
      childContentTypeVertices.add(natureNode);
    }
    childContentTypeVertices.addAll(helperModel.getNonNatureKlassNodes());
    childContentTypeVertices.addAll(helperModel.getTaxonomyVertices());
  
    List<Object> rids = new ArrayList<>();
    for (Vertex childContentTypeVertex : childContentTypeVertices) {
      rids.add(childContentTypeVertex.getId());
    }
  
    if (isValueInheritaceFromParent) {
      parentKlassIds.addAll(parentTaxonomyIds);
      String query = "select from (select expand (in('" + RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK + "')) from " + rids + ") where IN('" + RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK + "').code in " + EntityUtil.quoteIt(parentKlassIds);
      Iterable<Vertex> propagablePropertiesIntermediateNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      Map<String, Object> valueInheritanceFromParent =  new HashMap<>();
      referencedParentChildContextProperties.put(IReferencedParentChildContextPropertiesModel.VALUE_INHERITANCE_FROM_PARENT, valueInheritanceFromParent);
      fillContextualDataTransferProperties(valueInheritanceFromParent, propagablePropertiesIntermediateNodes);
    }
    if(isValueInheritaceToChildren) {
      String query = "select from (select expand (out('" + RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK + "')) from " + rids + ")";
      Iterable<Vertex> propagablePropertiesIntermediateNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      Map<String, Object> valueInheritanceToChildren = new HashMap<>();
      valueInheritanceToChildren = new HashMap<>();
      referencedParentChildContextProperties.put(IReferencedParentChildContextPropertiesModel.VALUE_INHERITANCE_TO_CHILDREN, valueInheritanceToChildren);
      fillContextualDataTransferProperties(valueInheritanceToChildren, propagablePropertiesIntermediateNodes);
    }
  }
  
  private void fillContextualDataTransferProperties(
      Map<String, Object> referencedContextualProperties,
      Iterable<Vertex> propagablePropertiesIntermediateNodes)
  {
    for (Vertex intermediateVertex : propagablePropertiesIntermediateNodes) {
      HashMap<String, Object> intermediateVertexMap = UtilClass.getMapFromNode(intermediateVertex);
      String contextKlassId = (String) intermediateVertexMap
          .get(IContextKlassModel.CONTEXT_KLASS_ID);
      String contextId = (String) intermediateVertexMap.get(IContextKlassModel.CONTEXT_ID);
      List<Edge> contextualPropagablePropertyLinks = IteratorUtils.toList(intermediateVertex
          .getEdges(Direction.OUT,
              RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK)
          .iterator());
      if (contextualPropagablePropertyLinks.isEmpty()) {
        continue;
      }
      Map<String, Object> referencedContextualPropertyMap = (Map<String, Object>) referencedContextualProperties
          .get(contextId);
      if (referencedContextualPropertyMap == null) {
        referencedContextualPropertyMap = new HashMap<>();
        referencedContextualPropertyMap.put(IReferencedContextPropertiesModel.CONTEXT_ID,
            contextId);
        referencedContextualPropertyMap.put(IReferencedContextPropertiesModel.CONTEXT_KLASS_ID,
            contextKlassId);
        referencedContextualPropertyMap.put(IReferencedContextPropertiesModel.ATTRIBUTES,
            new HashMap<>());
        referencedContextualPropertyMap.put(IReferencedContextPropertiesModel.TAGS,
            new HashMap<>());
        referencedContextualProperties.put(contextId, referencedContextualPropertyMap);
      }
      for (Edge contextualPropagablePropertyLink : contextualPropagablePropertyLinks) {
        Vertex propertyVertex = contextualPropagablePropertyLink.getVertex(Direction.IN);
        String couplingType = contextualPropagablePropertyLink
            .getProperty(ISectionElement.COUPLING_TYPE);
        String vertexType = propertyVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
        String code = UtilClass.getCodeNew(propertyVertex);
        if (vertexType.equals(VertexLabelConstants.ENTITY_TAG)) {
          Map<String, Object> tags = (Map<String, Object>) referencedContextualPropertyMap
              .get(IReferencedContextPropertiesModel.TAGS);
          mergeCouplingTypesForContextDataTransferableProperties(couplingType, code, tags);
        }
        else {
          Map<String, Object> attributes = (Map<String, Object>) referencedContextualPropertyMap
              .get(IReferencedContextPropertiesModel.ATTRIBUTES);
          mergeCouplingTypesForContextDataTransferableProperties(couplingType, code, attributes);
        }
      }
    }
  }
  
  private void mergeCouplingTypesForContextDataTransferableProperties(String couplingType,
      String code, Map<String, Object> property)
  {
    Map<String, String> propertyToPropagate = (Map<String, String>) property.get(code);
    if (propertyToPropagate == null) {
      propertyToPropagate = new HashMap<>();
      propertyToPropagate.put(IIdAndTypeModel.ID, code);
      propertyToPropagate.put(IIdAndTypeModel.TYPE, couplingType);
      property.put(code, propertyToPropagate);
    }
    else {
      String existingCouplingType = propertyToPropagate.get(IIdAndTypeModel.TYPE);
      if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)
          && !existingCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        propertyToPropagate.put(IIdAndTypeModel.TYPE, couplingType);
      }
      else if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)
          && existingCouplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
        propertyToPropagate.put(IIdAndTypeModel.TYPE, couplingType);
      }
    }
  }*/
  
  protected void mergeCouplingTypeOfReferencedElementsFromContext(Map<String, Object> mapToReturn,
      Vertex natureKlassNode, List<String> parentKlassIds, List<String> parentTaxonomies)
      throws Exception
  {
    if (((parentKlassIds == null || parentKlassIds.isEmpty()) && (parentTaxonomies == null
        || parentTaxonomies.isEmpty())) || natureKlassNode == null) {
      return;
    }
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Object rid = natureKlassNode.getId();
    parentKlassIds.addAll(parentTaxonomies);
    String query = "select from (select expand (in('"
        + RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK + "')) from " + rid
        + ") where IN('" + RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK
        + "').code in " + EntityUtil.quoteIt(parentKlassIds);
    Iterable<Vertex> propagablePropertiesIntermediateNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Vertex contextNode = VariantContextUtils.getContextNodeFromKlassNode(natureKlassNode);
    
    for (Vertex intermediateNode : propagablePropertiesIntermediateNodes) {
      Iterable<Edge> propertyEdges = intermediateNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
      for (Edge propertyEdge : propertyEdges) {
        String couplingType = propertyEdge.getProperty(ISectionElement.COUPLING_TYPE);
        Vertex property = propertyEdge.getVertex(Direction.IN);
        String propertyId = UtilClass.getCodeNew(property);
        Map<String, Object> referencedProperty = (Map<String, Object>) referencedElements
            .get(propertyId);
        if (referencedProperty == null) {
          continue;
        }
        if (referencedProperty.get(CommonConstants.ATTRIBUTE_VARIANT_CONTEXT) != null) {
          continue;
        }
        String existingCouplingType = (String) referencedProperty
            .get(IReferencedSectionElementModel.COUPLING_TYPE);
        if (couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
          referencedProperty.put(IReferencedSectionElementModel.COUPLING_TYPE,
              CommonConstants.DYNAMIC_COUPLED);
        }
        if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)
            && existingCouplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
          referencedProperty.put(IReferencedSectionElementModel.COUPLING_TYPE,
              CommonConstants.TIGHTLY_COUPLED);
        }
        
        List<Map<String, Object>> confictingSourceList = (List<Map<String, Object>>) referencedProperty
            .get(IReferencedSectionElementModel.CONFLICTING_SOURCES);
        if (confictingSourceList == null) {
          // No need to calculate conflicting source (This is null in case of
          // bulk propogation, etc
          // where conflicting source is of no use)
          // currently calculationg only of content open
          continue;
        }
        if ((couplingType.equals(CommonConstants.DYNAMIC_COUPLED))
            || (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)
                && !existingCouplingType.equals(CommonConstants.DYNAMIC_COUPLED))) {
          
          if (!existingCouplingType.equals(couplingType)
              && couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
            confictingSourceList.clear();
          }
          String type = (String) referencedProperty.get(IReferencedSectionElementModel.TYPE);
          Map<String, Object> confictingSourceMap = new HashMap<String, Object>();
          confictingSourceMap.put(IIdAndType.ID, UtilClass.getCodeNew(contextNode));
          confictingSourceMap.put(IIdAndType.TYPE, type);
          confictingSourceMap.put(IElementConflictingValuesModel.SOURCE_TYPE,
              CommonConstants.CONTEXT);
          confictingSourceList.add(confictingSourceMap);
        }
      }
    }
  }
  
  protected void fillReferencedLanguages(Map<String, Object> mapToReturn,
      List<String> languageCodes) throws Exception
  {
    if (!languageCodes.isEmpty()) {
      
      Iterable<Vertex> languageVertices = LanguageRepository.getLanguageInfo(languageCodes);
      
      Map<String, Object> referenceLanguages = (Map<String, Object>) mapToReturn
          .get(IGetConfigDetailsModel.REFERENCED_LANGUAGES);
      
      languageVertices.forEach(languageVertex -> {
        Map<String, Object> languageInfo = new HashMap<>();
        String languageCode = languageVertex.getProperty(ITaxonomy.CODE);
        languageInfo.put(IGetLanguagesInfoModel.ID, UtilClass.getCodeNew(languageVertex));
        languageInfo.put(IGetLanguagesInfoModel.LABEL,
            UtilClass.getValueByLanguage(languageVertex, ILanguage.LABEL));
        languageInfo.put(IGetLanguagesInfoModel.CODE, languageCode);
        languageInfo.put(IGetLanguagesInfoModel.DATE_FORMAT,
            languageVertex.getProperty(ILanguage.DATE_FORMAT));
        languageInfo.put(IGetLanguagesInfoModel.NUMBER_FORMAT,
            languageVertex.getProperty(ILanguage.NUMBER_FORMAT));
        languageInfo.put(IGetLanguagesInfoModel.LOCALE_ID,
            languageVertex.getProperty(ILanguage.LOCALE_ID));
        UtilClass.fetchIconInfo(languageVertex, languageInfo);
        referenceLanguages.put(languageCode, languageInfo);
      });
    }
  }

  /**
   * if you want to fill mandatory/should for attributes pass list of
   * mandatory/should attributes otherwise pass list of mandatory/should tags.
   *
   * @param mandatoryPropertyIds
   *          - list to be filled for mandatory attributes or tags
   * @param shouldPropertyIds
   *          - list to be filled for should attributes or tags
   * @param referencedElementMap
   */
  protected void fillMandatoryShouldPropertyIds(List<String> mandatoryPropertyIds,
      List<String> shouldPropertyIds, Map<String, Object> referencedElementMap)
  {
    String propertyId = (String) referencedElementMap.get(ISectionElement.PROPERTY_ID);
    if ((Boolean) referencedElementMap.get(ISectionElement.IS_MANDATORY)
        && !mandatoryPropertyIds.contains(propertyId)) {
      mandatoryPropertyIds.add(propertyId);
    }
    else if ((Boolean) referencedElementMap.get(ISectionElement.IS_SHOULD)
        && !mandatoryPropertyIds.contains(propertyId) && !shouldPropertyIds.contains(propertyId)) {
      shouldPropertyIds.add(propertyId);
    }
  }
}
