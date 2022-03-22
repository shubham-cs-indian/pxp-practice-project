package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForCustomTab;
import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class GetConfigDetailsWithoutPermissions extends AbstractConfigDetailsForCustomTab {
  
  public GetConfigDetailsWithoutPermissions(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsWithoutPermissions/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String processInstanceId = (String) requestMap
        .get(IMulticlassificationRequestModel.PROCESS_INSTANCE_ID);
    
    Map<String, Object> klassMap = null;
    if (processInstanceId != null) {
      klassMap = getConfigDetailWithoutPermissionsByProcessInstanceId(requestMap,
          processInstanceId);
    }
    else {
      klassMap = getConfigDetailWithoutPermissions(requestMap);
    }
    return klassMap;
  }
  
  private Map<String, Object> getConfigDetailWithoutPermissionsByProcessInstanceId(
      Map<String, Object> requestMap, String processInstanceId) throws Exception
  {
    Map<String, Object> klassMap = null;
    List<String> idsForGeneratingUniqueId = new ArrayList<>(
        (List<String>) requestMap.get(IMulticlassificationRequestModel.KLASS_IDS));
    idsForGeneratingUniqueId.addAll((Collection<? extends String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS));
    idsForGeneratingUniqueId.add(processInstanceId);
    idsForGeneratingUniqueId.add("ConfigDetailsWithoutPermissions");
    Collections.sort(idsForGeneratingUniqueId);
    String configDetailCacheVertexId = UtilClass.generateUniqueId(idsForGeneratingUniqueId);
    
    try {
      Vertex vertex = UtilClass.getVertexByIndexedId(configDetailCacheVertexId,
          VertexLabelConstants.CONFIG_DETAIL_CACHE);
      klassMap = UtilClass.getMapFromNode(vertex);
      klassMap.remove(CommonConstants.ID_PROPERTY);
      klassMap.remove(CommonConstants.PROCESS_INSTANCE_ID);
      klassMap.remove(CommonConstants.VERSION_ID);
      klassMap.remove(CommonConstants.CODE_PROPERTY);
    }
    catch (NotFoundException e) {
      klassMap = getConfigDetailWithoutPermissions(requestMap);
      
      Map<String, Object> mapToSave = new HashMap<>(klassMap);
      
      mapToSave.put(CommonConstants.ID_PROPERTY, configDetailCacheVertexId);
      mapToSave.put(CommonConstants.PROCESS_INSTANCE_ID, processInstanceId);
      
      Collection<Object> referencedDataRules = (Collection<Object>) klassMap
          .get(IGetConfigDetailsForCustomTabModel.REFERENCED_DATA_RULES);
      mapToSave.put(IGetConfigDetailsForCustomTabModel.REFERENCED_DATA_RULES,
          new ArrayList<>(referencedDataRules));
      
      OrientVertexType configDetailCacheVertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.CONFIG_DETAIL_CACHE, CommonConstants.CODE_PROPERTY);
      try {
        UtilClass.createNode(mapToSave, configDetailCacheVertexType, new ArrayList<>());
        UtilClass.getGraph()
            .commit();
      }
      catch (ORecordDuplicatedException ex) {
        
      }
    }
    
    return klassMap;
  }
  
  protected Map<String, Object> getConfigDetailWithoutPermissions(Map<String, Object> requestMap)
      throws Exception, KlassTaxonomyNotFoundException
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Boolean shouldSendTaxonomyHierarchies = (Boolean) requestMap
        .get(IMulticlassificationRequestModel.SHOULD_SEND_TAXONOMY_HIERARCHIES);
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap,
        shouldSendTaxonomyHierarchies);
    
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    Map<String, List<String>> tagIdTagValueIdsMap = (Map<String, List<String>>) requestMap
        .get(IMulticlassificationRequestModel.TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setTagIdTagValueIdsMap(tagIdTagValueIdsMap);
    Boolean shouldUseTagIdTagValueIdsMap = (Boolean) requestMap
        .get(IMulticlassificationRequestModel.SHOULD_USE_TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setShouldUseTagIdTagValueIdsMap(shouldUseTagIdTagValueIdsMap);
    
    String organizationId = (String) requestMap
        .get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String endpointId = (String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String physicalCatalogId = (String) requestMap
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    helperModel.setOrganizationId(organizationId);
    helperModel.setEndpointId(endpointId);
    helperModel.setPhysicalCatalogId(physicalCatalogId);
    
    // As in this call returns data for all klasses, taxonomy and collections of
    // Instance
    String requestedTypeId = CommonConstants.ALL_PROPERTY;
    
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    fillKlassDetails(mapToReturn, klassIds, referencedDataRuleMap, helperModel, requestedTypeId,
        UtilClass.getCodeNew(userInRole));
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    fillTaxonomyDetails(mapToReturn, taxonomyIds, referencedDataRuleMap, helperModel,
        requestedTypeId);
    List<String> collectionIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.COLLECTION_IDS);
    fillCollectionDetails(mapToReturn, collectionIds, helperModel, requestedTypeId);
    List<String> typeIds = new ArrayList<>();
    typeIds.addAll(klassIds);
    typeIds.addAll(taxonomyIds);
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(mapToReturn, typeIds);
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_KLASS_IDS);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_TAXONOMY_IDS);
    
    for (String parentKlassId : parentKlassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(parentKlassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.CLASSIFIER_IID);
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (isNature) {
          Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
          Integer numberOfVersionsToMaintain = (Integer) klassMap.get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
          Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
              .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN_FOR_PARENT);
          if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
            mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN_FOR_PARENT, numberOfVersionsToMaintain);
          }
        }
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
    
    if (shouldSendTaxonomyHierarchies) {
      consolidateTaxonomyHierarchyIds(taxonomyIds, mapToReturn);
    }
    
    fillReferencedContextLinkedToKlass(helperModel, mapToReturn);
    fillPersonalTaskIds(mapToReturn);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    UtilClass.isLanguageInheritanceHierarchyPresent(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    return mapToReturn;
  }
  
  protected void fillReferencedContextLinkedToKlass(IGetConfigDetailsHelperModel helperModel,
      Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    fillReferencedContextLinkedToKlass(embeddedContexts, referencedTags,
        helperModel);
  }
  
  protected void fillEntityIdsHavingReadPermission(Vertex userInRole,
      Map<String, Object> mapToReturn)
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Set<String> entitiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP);
    entitiesHavingReadPermission.addAll(CommonConstants.MODULE_ENTITIES);
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap,
      Boolean isGoldenRecord)
  {
    
    Map<String, Object> mapToReturn = super.getMapToReturn(referencedDataRuleMap);
    
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    Map<String, Object> referencedRoleMap = new HashMap<>();
    Map<String, String> referencedRelationshipsMapping = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS,
        languageVariantContexts);
    Map<String, Object> productVariantContexts = new HashMap<>();
    
    Map<String, String> relationshipReferencedElements = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipProperties);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES, referencedRoleMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING,
        referencedRelationshipsMapping);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.RELATIONSHIP_REFERENCED_ELEMENTS,
        relationshipReferencedElements);
    
    if (isGoldenRecord) {
      Map<String, Object> elementsConflictingValues = new HashMap<>();
      mapToReturn.put(IGetConfigDetailsForCustomTabModel.ELEMENTS_CONFLICTING_VALUES,
          elementsConflictingValues);
    }
    
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.TAXONOMY_HIERARCHIES, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_TAGS, new ArrayList<>());
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS, new ArrayList<>());
    return mapToReturn;
  }
  
  @Override
  protected void fillReferencedElementInRespectiveMap(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Set<String> attributeIds = helperModel.getAttributeIds();
    Set<String> roleIds = helperModel.getRoleIds();
    Set<String> tagIds = helperModel.getTagIds();
    
    for (String entityId : referencedElements.keySet()) {
      Map<String, Object> entity = new HashMap<>();
      if (attributeIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
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
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
        if (defaultValue == null || defaultValue.equals("")) {
          referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE,
              entity.get(IAttribute.DEFAULT_VALUE));
        }
        String defaultValueAsHtml = (String) referencedElementMap
            .get(ISectionAttribute.VALUE_AS_HTML);
        if (defaultValueAsHtml == null || defaultValueAsHtml.equals("")) {
          referencedElementMap.put(ISectionAttribute.VALUE_AS_HTML,
              entity.get(IAttribute.VALUE_AS_HTML));
        }
      }
      
      if (tagIds.contains(entityId)) {
        // Only filter tag values for types mentioned in list below
        List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
            SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
        Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
        if (helperModel.getShouldUseTagIdTagValueIdsMap()
            && tagTypes.contains(entityNode.getProperty(ITag.TAG_TYPE))) {
          Map<String, List<String>> tagIdTagValueIdsMap = helperModel.getTagIdTagValueIdsMap();
          List<String> tagValueIds = tagIdTagValueIdsMap.get(entityId);
          entity = TagUtils.getTagMapWithSelectTagValues(entityNode, tagValueIds);
        }
        else {
          entity = TagUtils.getTagMap(entityNode, false);
        }
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        List<String> selectedTagValuesList = (List<String>) referencedElementMap
            .remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        filterChildrenTagsInKlass(entity, selectedTagValuesList,
            (Map<String, Object>) referencedTags.get(entityId));
        
        if (!referencedTags.containsKey(entityId)) {
          referencedTags.put(entityId, entity);
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
      }
      
      if (roleIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ROLE);
        entity = (Map<String, Object>) referencedRoles.get(entityId);
        entity = RoleUtils.getRoleEntityMap(entityNode);
        referencedRoles.put(entityId, entity);
      }
    }
  }
  
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
}
