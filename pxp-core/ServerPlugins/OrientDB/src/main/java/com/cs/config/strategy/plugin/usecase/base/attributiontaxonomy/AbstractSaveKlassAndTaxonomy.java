package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionTagModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.onboarding.endpoint.AttributeMappingsException;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractSaveKlassAndTaxonomy extends AbstractOrientPlugin {

  protected static final String PARENT_CODE                  = "parentCode";
  protected static final String STATUS_TAGS                  = "statusTags";
  protected static final String TASKS                        = "tasks";
  protected static final String ADDED_TASKS                  = "addedTasks";
  protected static final String DELETED_TASKS                = "deletedTasks";
  protected static final String EMBEDDED_CLASSES             = "embeddedClasses";
  protected static final String COUPLINGS                    = "couplings";
  protected static final String REFERENCES                   = "references";
  protected static final String ADDED_RELATIONSHIPS          = "addedRelationships";
  protected static final String VERSION_CONTEXT_CODE         = "versionContextCode";
  protected static final String PRODUCT_VARIANT_CONTEXT_CODE = "productVariantContextCode";
  protected static final String ADDED_CONTEXTS               = "addedContexts";
  protected static final String VERSION_CONTEXTS             = "versionContexts";
  protected static final String CONTEXT_CODE                 = "contextCode";
  protected static final String LEVEL_CODE                   = "levelCode";
  protected static final String MASTER_PARENT_TAG            = "masterParentTag";
  protected static final String LEVEL_LABELS                 = "levelLabels";
  protected static final String TAXONOMY_TYPE                = "taxonomyType";
  protected static final String REFERENCED_CLASS_IDS         = "referencedClassIds";
  protected static final String REFERENCED_ATTRIBUTE_IDS     = "referencedAttributeIds";
  protected static final String DELETED_STRUCTURES           = "deletedStructures";
  protected static final String STRUCTURE_CHILDREN           = "structureChildren";
  protected static final String KLASS_VIEW_SETTING           = "klassViewSetting";
  protected static final String MODIFIED_STRUCTURES          = "modifiedStructures";
  protected static final String ADDED_STRUCTURES             = "addedStructures";
  protected static final String CODE_AND_IID_SEPERATOR       = ":";

  public AbstractSaveKlassAndTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  protected void processSections(Map<String, Object> entityMap, Vertex entityNode, String vertexType, IExceptionModel failure) throws Exception
  {
    Map<String, Object> klassMap = new HashMap<>();
    KlassUtils.addSectionsToKlassEntityMap(entityNode, klassMap, true);
    List<Map<String, Object>> sections = (List<Map<String, Object>>) klassMap.get(CommonConstants.SECTIONS_PROPERTY);

    Map<String, Object> existingSections = getExistingSections(sections);
    Map<String, Object> sectionsToImport = (HashMap<String, Object>) entityMap.get(CommonConstants.SECTIONS_PROPERTY);
    Map<String, Object> validProperty = ImportUtils.getValidProperty(sectionsToImport, VertexLabelConstants.PROPERTY_COLLECTION, failure);
    List<String> sectionsNotInherited = existingSections.entrySet()
        .stream()
        .filter(x -> !(Boolean) ((Map<String, Object>) x.getValue()).get(CommonConstants.IS_INHERITED_PROPERTY))
        .map(x -> x.getKey())
        .collect(Collectors.toList());

    //existing sections not present in import need to be deleted
    Collection<String> sectionsCodesToDelete = CollectionUtils.subtract(sectionsNotInherited, validProperty.keySet());
    Collection<String> sectionCodesToAdd = CollectionUtils.subtract(validProperty.keySet(), existingSections.keySet());
    Collection<String> modifiedSections = CollectionUtils.intersection(existingSections.keySet(), validProperty.keySet());

    List<Map<String, Object>> modifiedElements = new ArrayList<>();
    for (Entry<String, Object> sectionToImport : validProperty.entrySet()) {
      if (modifiedSections.contains(sectionToImport.getKey())) {
        List<Map<String, Object>> values = (List<Map<String, Object>>) sectionToImport.getValue();
        Map<String, Object> existingSection = (Map<String, Object>) existingSections.get(sectionToImport.getKey());
        Map<String, Object> existingElements = (Map<String, Object>) existingSection.get(CommonConstants.ELEMENTS_PROPERTY);

        for (Map<String, Object> value : values) {

          String code = (String) value.get(CommonConstants.CODE_PROPERTY);
          Map<String, Object> existingElement = (Map<String, Object>) existingElements.get(code);
          Boolean isInherited = (Boolean) value.get(CommonConstants.IS_INHERITED_PROPERTY);
          if (!isInherited) {
            if(existingElement == null) {
              existingElement = new HashMap<>();
              existingElement.putAll(value);
            }
            existingElement.put(CommonConstants.IS_INHERITED_PROPERTY, false);
            try {
               prepareModifiedElements(entityNode, modifiedElements, value, existingElement, failure);
            }catch(NotFoundException e){
              ExceptionUtil.addFailureDetailsToFailureObject(failure, e, code, e.getMessage());
            }
          }
        }
      }
    }

    List<String> idsToDelete = sections.stream()
        .filter(x -> sectionsCodesToDelete.contains(x.get(CommonConstants.CODE_PROPERTY)))
        .map(x -> (String)x.get(CommonConstants.ID_PROPERTY))
        .collect(Collectors.toList());

    Map<String, Object> sectionsADM = new HashMap<>();
    sectionsADM.put(IKlassSaveModel.DELETED_SECTIONS, idsToDelete);
    sectionsADM.put(IKlassSaveModel.MODIFIED_SECTIONS, new ArrayList<>());
    sectionsADM.put(IKlassSaveModel.ADDED_SECTIONS, getSectionsToAdd(sectionCodesToAdd));
    sectionsADM.put(IKlassSaveModel.MODIFIED_ELEMENTS, modifiedElements);
    try {
      processSectionADM(entityNode, vertexType, sectionsADM);
      if (!sectionCodesToAdd.isEmpty()) {
        processSections(entityMap, entityNode, vertexType, failure);
      }
    }catch (Exception e) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, e, "Sections", e.getMessage());
    }
  }

  private List<Map<String, Object>> getSectionsToAdd(Collection<String> sectionCodesToAdd)
  {
    List<Map<String, Object>> sectionsToAdd = new ArrayList<>();
    for(String sectionCode : sectionCodesToAdd) {
      Map<String, Object> sectionToAdd= new HashMap<>();
      sectionToAdd.put(ISection.PROPERTY_COLLECTION_ID, sectionCode);
      sectionToAdd.put( CommonConstants.TYPE, CommonConstants.SECTION_TYPE);
      sectionsToAdd.add(sectionToAdd);
    }
    return sectionsToAdd;
  }

  private void processSectionADM(Vertex entityNode, String vertexType, Map<String, Object> sectionsADM) throws Exception
  {
    List<Vertex> klassAndChildNodes = KlassUtils.getKlassAndChildNodes(entityNode);
    List<Long> updatedMandatoryPropertyIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    Map<String, Object> propertiesADMMap = new HashMap<>();
    List<String> addedCalculatedAttributeIds = new ArrayList<>();
    Map<String, Object> removedAttributeVariantContextsMap = prepareRemoveVariantMap();
    
    SaveKlassUtil.manageSectionsInKlass(entityNode, sectionsADM, klassAndChildNodes, vertexType, defaultValueChangeList, deletedPropertyMap,
        propertiesADMMap, removedAttributeVariantContextsMap, updatedMandatoryPropertyIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
  }
  
  private Map<String, Object> getExistingSections(List<Map<String, Object>>  sections) throws Exception
  {
    Function<Object, Map<String, Object>> x = o -> ((List<Map<String, Object>>) o).stream()
        .collect(Collectors.toMap(m -> (String) m.get(CommonConstants.PROPERTY_ID), m -> m));

    Map<String, Object> existingSections = new HashMap<>();
    for (Map<String, Object> section : sections) {
      Map<String, Object> elements = x.apply(section.get(CommonConstants.ELEMENTS_PROPERTY));
      Map<String, Object> existingSection = new HashMap<>();
      existingSection.put(CommonConstants.ELEMENTS_PROPERTY, elements);
      existingSection.put(CommonConstants.IS_INHERITED_PROPERTY, section.get(ISection.IS_INHERITED));
      existingSections.put((String) section.get(CommonConstants.CODE_PROPERTY), existingSection);
    }
    return existingSections;
  }

  private Map<String, Object> prepareRemoveVariantMap()
  {
    Map<String, Object> removeAttributeVarinat = new HashMap<>();
    removeAttributeVarinat.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<>());
    removeAttributeVarinat.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<>());
    return removeAttributeVarinat;
  }
  
  private void prepareModifiedElements(Vertex taxonomyNode, List<Map<String, Object>> modifiedElements, Map<String, Object> newElement,
      Map<String, Object> existingProperty, IExceptionModel failure) throws Exception
  {
    try {
      String code = (String) newElement.get(CommonConstants.CODE_PROPERTY);
      String type = (String) existingProperty.get(CommonConstants.TYPE_PROPERTY);
      existingProperty.put(IModifiedSectionElementModel.IS_MODIFIED, true);
      existingProperty.put(ISectionAttribute.COUPLING_TYPE, newElement.get(ISectionAttribute.COUPLING_TYPE));
      existingProperty.put(ISectionAttribute.IS_MANDATORY, newElement.get(ISectionAttribute.IS_MANDATORY));
      existingProperty.put(ISectionAttribute.IS_SHOULD, newElement.get(ISectionAttribute.IS_SHOULD));
      existingProperty.put(ISectionAttribute.IS_SKIPPED, newElement.get(ISectionAttribute.IS_SKIPPED));
      existingProperty.put(ISectionAttribute.IS_VERSIONABLE, newElement.get(ISectionAttribute.IS_VERSIONABLE));
      
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        existingProperty.put(ISectionAttribute.PRECISION, newElement.get(ISectionAttribute.PRECISION));
        prepareAttributeProperty(taxonomyNode, newElement, existingProperty, failure);
      }
      else if (type.equals(CommonConstants.TAG)) {
        prepareTagProperty(newElement, existingProperty);
      }
      else if (type.equals(CommonConstants.TAXONOMY)) {
        existingProperty.put(IModifiedSectionTagModel.IS_MULTI_SELECT, newElement.get(IModifiedSectionTagModel.IS_MULTI_SELECT));
      }
      modifiedElements.add((HashMap<String, Object>) existingProperty);
    }
    catch (NotFoundException e) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, e, (String) newElement.get(CommonConstants.CODE_PROPERTY), "Not Found");
    }
  }

  private void prepareAttributeProperty(Vertex taxonomyNode, Map<String, Object> newElement, Map<String, Object> existingProperty,
      IExceptionModel failure)
  {
    existingProperty.put(ISectionAttribute.IS_IDENTIFIER, newElement.get(ISectionAttribute.IS_IDENTIFIER));
    existingProperty.put(ISectionAttribute.DEFAULT_VALUE, newElement.get(ISectionAttribute.DEFAULT_VALUE));
    existingProperty.put(ISectionAttribute.VALUE_AS_HTML, newElement.get(ISectionAttribute.VALUE_AS_HTML));
    String attributeVariant = (String) newElement.get(ConfigTag.attributeVariantContextCode.toString());
    validateAttributeVariants(taxonomyNode, existingProperty, failure, attributeVariant);
  }

  private void validateAttributeVariants(Vertex taxonomyNode, Map<String, Object> existingProperty, IExceptionModel failure,
      String attributeVariant)
  {
    if (StringUtils.isNoneEmpty(attributeVariant)) {
      try {
        Vertex variant = UtilClass.getVertexByCode(attributeVariant, VertexLabelConstants.VARIANT_CONTEXT);
        String variantType = variant.getProperty(CommonConstants.TYPE_PROPERTY);
        if (CommonConstants.ATTRIBUTE_VARIANT_CONTEXT.equals(variantType))
          existingProperty.put(IModifiedSectionElementModel.ATTRIBUTE_VARIANT_CONTEXT, attributeVariant);
        else
          throw new Exception(
              "Only Attribute variant will attached to element variant code" + attributeVariant + "is of type " + variantType);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, taxonomyNode.getProperty(CommonConstants.CODE_PROPERTY),
            attributeVariant);
      }
    }
  }

  private void prepareTagProperty(Map<String, Object> newElement, Map<String, Object> existingProperty) throws Exception
  {
    String tagCode = (String) existingProperty.get(CommonConstants.PROPERTY_ID);
    Vertex tagVertex = UtilClass.getVertexByCode(tagCode, VertexLabelConstants.ENTITY_TAG);
    List<String> tagValues = tagVertex.getProperty(ITagModel.TAG_VALUES_SEQUENCE);
    String aggDefaultValues =  (String) newElement.get(ISectionAttribute.DEFAULT_VALUE);
    List<Map<String, Object>> addedDefaultValues = new ArrayList<>();
    if (StringUtils.isNotEmpty(aggDefaultValues)) {
      prepareTagValues(aggDefaultValues, tagValues, addedDefaultValues);
    }
    List<String> selectedTagValues = (List<String>) newElement.get(ConfigTag.selectedTagValues.toString());
    if (selectedTagValues == null) {
      selectedTagValues = new ArrayList<>();
    }
    //only valid tag values will set
    selectedTagValues.retainAll(tagValues);
    existingProperty.put(IModifiedSectionTagModel.ADDED_DEFAULT_VALUES, addedDefaultValues);
    existingProperty.put(IModifiedSectionTagModel.MODIFIED_DEFAULT_VALUES, new ArrayList<>());
    existingProperty.put(IModifiedSectionTagModel.DELETED_DEFAULT_VALUES, new ArrayList<>());
    existingProperty.put(IModifiedSectionTagModel.ADDED_SELECTED_TAG_VALUES, selectedTagValues);
    existingProperty.put(IModifiedSectionTagModel.DELETED_SELECTED_TAG_VALUES, new ArrayList<>());
    existingProperty.put(IModifiedSectionTagModel.IS_MULTI_SELECT, newElement.get(IModifiedSectionTagModel.IS_MULTI_SELECT));
    existingProperty.put(IModifiedSectionTagModel.TAG_TYPE, newElement.get(ISectionTag.TAG_TYPE));
   }
  
  private List<Map<String, Object>> prepareTagValues(String aggTagValueCodes, List<String> tagValues, List<Map<String, Object>> addedDefaultValues)
  {
    String[] tagValueCodes = aggTagValueCodes.split(",");
    for (int i = 0; i < tagValueCodes.length; i++) {
      String tagValueCode = tagValueCodes[i];
      if (!tagValueCode.isEmpty() && tagValues.contains(tagValueCode)) {
        Map<String, Object> tagValueMap = new HashMap<>();
        tagValueMap.put(IIdRelevance.TAGID, tagValueCode);
        tagValueMap.put(IIdRelevance.RELEVANCE, 100);
        addedDefaultValues.add(tagValueMap);
      }
    }
    return addedDefaultValues;
  }

  protected void handleEmbeddedClasses(Map<String, Object> klassMap, Vertex klassNode, IExceptionModel failure) throws Exception
  {
    String entityCode = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    Map<String, Object> contextInfo = (Map<String, Object>) klassMap.get(ConfigTag.embeddedClasses.toString());
    Map<String, Object> validClassesToImport = getValidClasses(klassNode, failure, contextInfo);

    Iterable<Vertex> existingNodes = klassNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    Map<String, Object> existingClasses = new HashMap<>();
    for (Vertex existingNode : existingNodes) {
      existingClasses.put(existingNode.getProperty(CommonConstants.CODE_PROPERTY), UtilClass.getMapFromNode(existingNode));
    }

    Set<String> classesToAdd = SetUtils.difference(validClassesToImport.keySet(), existingClasses.keySet());
    handleAddedEmbeddedClasses(klassNode, failure, contextInfo, classesToAdd);

    Set<String> classesToDelete = SetUtils.difference(existingClasses.keySet(), validClassesToImport.keySet());
    handleRemovedEmbeddedClasses(klassNode, classesToDelete);

    Set<String> classesToModify = SetUtils.intersection(existingClasses.keySet(), validClassesToImport.keySet());
    handleModifiedEmbeddedClasses(klassNode, failure, entityCode, contextInfo, classesToModify);
  }



  protected void handleModifiedEmbeddedClasses(Vertex klassNode, IExceptionModel failure, String entityCode,
      Map<String, Object> contextInfo, Set<String> classesToModify) throws Exception
  {
    List<Map<String, Object>> modifiedEmbeddedClasses = new ArrayList<>();

    for (String classToModify : classesToModify) {
      try {
        Map<String, Object> modifiedEmbeddedClass = prepareModifiedEmbeddedClass(classToModify);
        Iterable<Edge> existingCoupledProperties = getCoupledProperties(entityCode, classToModify);
        Map<String, Edge> propertyCodeVSEdge = new HashMap<>();

        for (Edge coupledProperty : existingCoupledProperties) {
          String propertyCode = coupledProperty.getVertex(Direction.IN).getProperty(CommonConstants.CODE_PROPERTY);
          propertyCodeVSEdge.put(propertyCode, coupledProperty);
        }

        Map<String, Object> context = (Map<String, Object>) contextInfo.get(classToModify);
        List<Map<String, Object>> importCoupledProperties = (List<Map<String, Object>>) context.get(ConfigTag.couplings.toString());

        for (Map<String, Object> importCoupledProperty : importCoupledProperties) {
          handleCoupledProperties(klassNode, failure, modifiedEmbeddedClass, propertyCodeVSEdge, importCoupledProperty);
        }
        fillDeletedCoupledProperties(modifiedEmbeddedClass, propertyCodeVSEdge);
        if (isClassChanged(modifiedEmbeddedClass)) {
          modifiedEmbeddedClasses.add(modifiedEmbeddedClass);
        }
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, classToModify, klassNode.getProperty(IKlass.CODE));
      }
      VariantContextUtils.manageModifiedContextKlasses(klassNode, modifiedEmbeddedClasses, new HashMap<>());
    }
  }

  protected void fillDeletedCoupledProperties(Map<String, Object> modifiedEmbeddedClass, Map<String, Edge> propertyCodeVSEdge)
  {
    for (Map.Entry<String, Edge> entry : propertyCodeVSEdge.entrySet()) {
      String type = entry.getValue().getVertex(Direction.IN).getProperty(CommonConstants.TYPE);
      String deleted = type.equals(CommonConstants.ATTRIBUTE) ?
          IModifiedContextKlassModel.DELETED_ATTRIBUTES :
          IModifiedContextKlassModel.DELETED_TAGS;
      List<String> deletedEntities = (List) modifiedEmbeddedClass.get(deleted);
      deletedEntities.add(entry.getKey());
    }
  }

  protected void handleCoupledProperties(Vertex klassNode, IExceptionModel failure, Map<String, Object> modifiedEmbeddedClass,
      Map<String, Edge> propertyCodeVSEdge, Map<String, Object> importCoupledProperty)
  {
    try {
      String importPropertyCode = (String) importCoupledProperty.get(CommonConstants.CODE_PROPERTY);
      String propertyType = (String) importCoupledProperty.get(CommonConstants.TYPE);
      importCoupledProperty.put(CommonConstants.ID_PROPERTY, importPropertyCode);
      switch (propertyType) {
        case CommonConstants.ATTRIBUTE:
          Vertex attribute = UtilClass.getVertexByCode(importPropertyCode, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          if (List.of(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE, CommonConstants.CALCULATED_ATTRIBUTE_TYPE)
              .contains(attribute.getProperty(IAttribute.TYPE))) {
            throw new AttributeMappingsException("Attribute of Calculated And Concatenated Type should not be flowed");
          }
          isCreatedOrModified(modifiedEmbeddedClass, propertyCodeVSEdge, importCoupledProperty, importPropertyCode,
              IModifiedContextKlassModel.ADDED_ATTRIBUTES, IModifiedContextKlassModel.MODIFIED_ATTRIBUTES);
          break;
        case CommonConstants.TAG:
          UtilClass.getVertexByCode(importPropertyCode, VertexLabelConstants.ENTITY_TAG);
          isCreatedOrModified(modifiedEmbeddedClass, propertyCodeVSEdge, importCoupledProperty, importPropertyCode,
              IModifiedContextKlassModel.ADDED_TAGS, IModifiedContextKlassModel.MODIFIED_TAGS);
          break;
      }
    }
    catch (Exception e) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, klassNode.getProperty(IKlass.CODE));
    }
  }

  private void isCreatedOrModified(Map<String, Object> modifiedEmbeddedClass, Map<String, Edge> propertyCodeVSEdge,
      Map<String, Object> importCoupledProperty, String importPropertyCode,String creationKey, String modificationKey)
  {
    if (propertyCodeVSEdge.containsKey(importPropertyCode)) {
      String couplingType = propertyCodeVSEdge.get(importPropertyCode).getProperty(ConfigTag.couplingType.name());
      String importCouplingType = (String) importCoupledProperty.get(ConfigTag.couplingType.name());

      if(!couplingType.equals(importCouplingType)){
        List<Map<String, Object>> modified = (List<Map<String, Object>>) modifiedEmbeddedClass.get(modificationKey);
        modified.add(importCoupledProperty);
        propertyCodeVSEdge.remove(importPropertyCode);
      }
    }
    else {
      List<Map<String, Object>> added = (List<Map<String, Object>>) modifiedEmbeddedClass.get(creationKey);
      added.add(importCoupledProperty);
    }
  }

  protected boolean isClassChanged(Map<String, Object> modifiedEmbeddedClass)
  {
    boolean isAddedAttributeEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.ADDED_ATTRIBUTES)).isEmpty();
    boolean isAddedTagEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.ADDED_TAGS)).isEmpty();
    boolean isModifiedAttributeEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.MODIFIED_ATTRIBUTES)).isEmpty();
    boolean isModifiedTagEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.MODIFIED_TAGS)).isEmpty();
    boolean isDeletedAttributeEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.DELETED_ATTRIBUTES)).isEmpty();
    boolean isDeletedTagsEmpty = ((List) modifiedEmbeddedClass.get(IModifiedContextKlassModel.DELETED_TAGS)).isEmpty();

    return !isAddedAttributeEmpty || !isAddedTagEmpty || !isModifiedAttributeEmpty || !isModifiedTagEmpty || !isDeletedAttributeEmpty
        || !isDeletedTagsEmpty;
  }

  protected Map<String, Object> prepareModifiedEmbeddedClass(String classToModify)
  {
    Map<String, Object> modifiedEmbeddedClass = new HashMap<>();
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.CONTEXT_KLASS_ID, classToModify);
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.ADDED_ATTRIBUTES, new ArrayList<>());
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.ADDED_TAGS, new ArrayList<>());
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.MODIFIED_ATTRIBUTES, new ArrayList<>());
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.MODIFIED_TAGS, new ArrayList<>());
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.DELETED_ATTRIBUTES, new ArrayList<>());
    modifiedEmbeddedClass.put(IModifiedContextKlassModel.DELETED_TAGS, new ArrayList<>());
    return modifiedEmbeddedClass;
  }

  protected Iterable<Edge> getCoupledProperties(String entityCode, String code) throws NotFoundException
  {
    String queryFormat = "select from "+ VertexLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES +" where contextKlassId = '%s' and  " +
        "in('"+ RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK +"').code = '%s'";
    String query = String.format(queryFormat, code, entityCode);
    Iterable<Vertex> intermediateVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    Iterator<Vertex> iterator = intermediateVertices.iterator();
    if(!iterator.hasNext()){
      throw new NotFoundException();
    }
    Vertex intermediateNode = iterator.next();
    Iterable<Edge> properties = intermediateNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
    return properties;
  }

  protected void handleAddedEmbeddedClasses(Vertex klassNode, IExceptionModel failure, Map<String, Object> contextInfo,
      Set<String> classesToAdd) throws Exception
  {
    List<Map<String, Object>> addedContextClasses = new ArrayList<>();
    for (String classToAdd : classesToAdd) {

      Map<String, Object> embeddedClass = new HashMap<>();
      embeddedClass.put(IContextKlassModel.CONTEXT_KLASS_ID, classToAdd);
      embeddedClass.put(IContextKlassModel.ATTRIBUTES, new ArrayList<>());
      embeddedClass.put(IContextKlassModel.TAGS, new ArrayList<>());

      Map<String, Object> contextData = (Map<String, Object>) contextInfo.get(classToAdd);
      List<Map<String, Object>> couplingInfo = (List<Map<String, Object>>) contextData.get(ConfigTag.couplings.toString());
      if (couplingInfo != null) {
        for (Map<String, Object> coupling : couplingInfo) {
          try {
            String code = (String) coupling.get(CommonConstants.CODE_PROPERTY);
            String propertyType = (String) coupling.get(CommonConstants.TYPE);
            coupling.put(CommonConstants.ID_PROPERTY, code);
            switch (propertyType) {
              case CommonConstants.ATTRIBUTE:
                Vertex attribute = UtilClass.getVertexByCode(code, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
                if (List.of(CommonConstants.CONCATENATED_ATTRIBUTE_TYPE, CommonConstants.CALCULATED_ATTRIBUTE_TYPE)
                    .contains(attribute.getProperty(IAttribute.TYPE))) {
                  throw new AttributeMappingsException("Attribute of Calculated And Concatenated Type should not be flowed");
                }
                ((List) embeddedClass.get(IContextKlassModel.ATTRIBUTES)).add(coupling);
                break;
              case CommonConstants.TAG:
                UtilClass.getVertexByCode(code, VertexLabelConstants.ENTITY_TAG);
                ((List) embeddedClass.get(IContextKlassModel.TAGS)).add(coupling);
                break;
              case CommonConstants.RELATIONSHIP:
                UtilClass.getVertexByCode(code, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
                break;
            }
          }
          catch (Exception e) {
            ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, klassNode.getProperty(IKlass.CODE));
          }
        }
      }
      addedContextClasses.add(embeddedClass);
    }
    VariantContextUtils.manageAddedContextKlasses(klassNode, addedContextClasses, new HashMap<>());
  }

  protected void handleRemovedEmbeddedClasses(Vertex klassNode, Set<String> classesToDelete)
  {
    if(!classesToDelete.isEmpty()){
      VariantContextUtils.manageDeletedContextKlasses(klassNode, new ArrayList<>(classesToDelete), new HashMap<>(), new ArrayList<>());
    }
  }

  protected Map<String, Object> getValidClasses(Vertex klassNode, IExceptionModel failure, Map<String, Object> contextInfo) throws Exception
  {
    Map<String, Object> validEmbeddedClasses = VariantContextUtils.getValidEmbeddedClasses(contextInfo.keySet());
    Set<String> invalidClasses = SetUtils.difference(contextInfo.keySet(), validEmbeddedClasses.keySet());
    for(String invalidClass : invalidClasses){
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new InvalidClassException("Class is not present or is not embedded")  , invalidClass,
          klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return validEmbeddedClasses;
  }

  protected void manageTasks(Map<String,Object> entity, Vertex entityNode, IExceptionModel failure) throws Exception
  {
    List<String> tasks = (List<String>) entity.get(TASKS);
    List<String> validTasks = ImportUtils.getValidNodeCodes(tasks, VertexLabelConstants.ENTITY_TYPE_TASK, failure,
        entityNode.getProperty(CommonConstants.CODE_PROPERTY));
    Map<Boolean, List<String>> delta = ImportUtils.prepareDelta(entityNode, Direction.OUT, RelationshipLabelConstants.HAS_TASK, validTasks);
    Map<String, Object> taskAD = Map.of(IKlassSaveModel.ADDED_TASKS, delta.get(Boolean.TRUE), IKlassSaveModel.DELETED_TASKS, delta.get(Boolean.FALSE));
    SaveKlassUtil.manageTasks(taskAD, entityNode);
  }
}
