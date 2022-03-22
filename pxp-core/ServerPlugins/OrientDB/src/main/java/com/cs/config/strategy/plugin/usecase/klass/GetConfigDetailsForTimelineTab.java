package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForCustomTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTimelineTab extends AbstractConfigDetailsForCustomTab {
  
  public GetConfigDetailsForTimelineTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTimelineTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    Map<String, List<String>> tagIdTagValueIdsMap = (Map<String, List<String>>) requestMap
        .get(IMulticlassificationRequestModel.TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setTagIdTagValueIdsMap(tagIdTagValueIdsMap);
    Boolean shouldUseTagIdTagValueIdsMap = (Boolean) requestMap
        .get(IMulticlassificationRequestModel.SHOULD_USE_TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setShouldUseTagIdTagValueIdsMap(shouldUseTagIdTagValueIdsMap);
    
    helperModel
        .setEndpointId((String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID));
    helperModel.setOrganizationId(
        (String) requestMap.get(IMulticlassificationRequestModel.ORAGANIZATION_ID));
    helperModel.setPhysicalCatalogId(
        (String) requestMap.get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID));
    
    // As in this call returns data for all klasses, taxonomy and collections of
    // Instance
    String requestedTypeId = CommonConstants.ALL_PROPERTY;
    
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    helperModel.setInstanceKlassIds(new HashSet<>(klassIds));
    fillKlassDetails(mapToReturn, klassIds, referencedDataRuleMap, helperModel, requestedTypeId,
        UtilClass.getCodeNew(userInRole));
    ConfigDetailsUtils.fillLinkedVariantsConfigInfo(mapToReturn, klassIds);
    
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
    fillReferencedPermission(mapToReturn, CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE, userInRole,
        helperModel);
    fillTemplateDetails(mapToReturn, helperModel, requestedTypeId);
    fillPersonalTaskIds(mapToReturn);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    filterOutReferencedElements(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    
    return mapToReturn;
  }
  
  protected void fillTemplateDetails(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, String requestedTypeId) throws Exception
  {
    Map<String, Object> referencedTemplate = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TEMPLATE, referencedTemplate);
    List<Map<String, Object>> tabsList = new ArrayList<>();
    referencedTemplate.put(ITemplate.TABS, tabsList);
    Set<String> tabIds = new HashSet<>();
    if (requestedTypeId.equals(CommonConstants.ALL_PROPERTY)) {
      referencedTemplate.put(ITemplate.TYPE, CommonConstants.CUSTOM_TEMPLATE);
      referencedTemplate.put(ITemplate.ID, CommonConstants.ALL_PROPERTY);
      referencedTemplate.put(ITemplate.LABEL, "All");
      fillTabIdsAssociatedWithNatureKlass(helperModel, tabIds, mapToReturn);
      fillTabIdsAssociatedWithNonNatureKlasses(helperModel, tabIds, mapToReturn);
      fillTabIdsAssociatedWithTaxonomies(helperModel, tabIds, mapToReturn);
      fillTabsIdsAssociatedCollections(helperModel, tabIds, mapToReturn);
    }
    else {
      Vertex requestedTypeVertex = helperModel.getRequestedTypeVertex();
      Map<String, Object> requestedTypeMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, ITemplate.LABEL), requestedTypeVertex);
      referencedTemplate.put(ITemplate.TYPE, CommonConstants.CUSTOM_TEMPLATE);
      referencedTemplate.put(ITemplate.ID, requestedTypeMap.get(ITemplate.ID));
      referencedTemplate.put(ITemplate.LABEL, requestedTypeMap.get(ITemplate.LABEL));
      fillTabIdsAssociatedWithRequestedType(helperModel, tabIds, mapToReturn);
    }
    // add default runtime tabs i.e, task, event and timeline tab
    tabIds.addAll(getDefaultRuntimeTabs(helperModel));
    
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    sequenceList.retainAll(tabIds);
    
    for (String tabId : sequenceList) {
      Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      List<String> propertySequenceList = (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST);
      propertySequenceList.retainAll(helperModel.getAssociatedSectionIds());
      tabMap.put(ITemplateTab.BASE_TYPE, getTabTypeById(tabId));
      tabsList.add(tabMap);
      if (tabId.equals(SystemLevelIds.TIMELINE_TAB)) {
        tabMap.put(ITemplateTab.IS_SELECTED, true);
      }
    }
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
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
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_TAGS, new ArrayList<>());
    
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS, new ArrayList<>());
    
    return mapToReturn;
  }
  
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
          entity = TagUtils.getTagMap(entityNode, true);
        }
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
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
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, String tabType,
      Vertex userInRole, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Set<Vertex> roles = new HashSet<Vertex>();
    // Get all roles & pass to GlobalPermissionUtil to get task class Ids that
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> roleVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roles.add(roleVertex);
    }
    fillRoleIdsAndTaskIdsHavingReadPermission(responseMap, userInRole, roles);
    
    fillKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole, responseMap);
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    String roleId = UtilClass.getCodeNew(userInRole);
    
    fillGlobalPermissionDetails(roleId, responseMap, helperModel);
    
    fillNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillNonNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillTaxonomyPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillCollectionPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    filterContextAccordingToKlassPermission(responseMap, helperModel, userInRole);
    
    fillFunctionPermissionDetails(userInRole, responseMap);
  }
}
