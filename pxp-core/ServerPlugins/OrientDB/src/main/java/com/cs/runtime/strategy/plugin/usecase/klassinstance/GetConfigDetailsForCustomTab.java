package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForCustomTab;
import com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util.RelationshipInheritanceUtil;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.collections.IteratorUtils;

import java.util.*;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForCustomTab extends AbstractConfigDetailsForCustomTab {
  
  public GetConfigDetailsForCustomTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForCustomTab/*" };
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

    String requestedTypeId = (String) requestMap.get(IMulticlassificationRequestModel.TYPE_ID);
    requestedTypeId = requestedTypeId == null ? CommonConstants.ALL_PROPERTY : requestedTypeId;
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
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_KLASS_IDS);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.PARENT_TAXONOMY_IDS);
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    fillReferencedPermission(mapToReturn, CommonConstants.CUSTOM_TEMPLATE, userInRole, helperModel);

    String requestedTabId = (String) requestMap.get(IMulticlassificationRequestModel.TAB_ID);
    String requestedTemplateId = (String) requestMap
        .get(IMulticlassificationRequestModel.TEMPLATE_ID);
    if (requestedTemplateId == null) {
      fillTemplateDetails(mapToReturn, helperModel, requestedTabId, requestedTypeId,
          (Boolean) requestMap.get(IMulticlassificationRequestModel.IS_GRID_EDITABLE));
    }
    else {
      fillTemplateDetailsForCustomTemplate(mapToReturn, helperModel, requestedTabId,
          requestedTemplateId);
    }

    fillReferencedContextLinkedToKlass(helperModel, mapToReturn);
    fillPersonalTaskIds(mapToReturn);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    filterOutReferencedElements(mapToReturn, helperModel);
    filterOutReferencedElementsForNatureRelationship(mapToReturn, helperModel);


    fillMandatoryReferencedAttributes(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    RelationshipInheritanceUtil.fillRelationshipInheritanceInfo(mapToReturn, klassIds);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.ROLE_ID_OF_CURRENT_USER,
        UtilClass.getCodeNew(userInRole));

    return mapToReturn;
  }
  
  private void filterOutReferencedElementsForNatureRelationship(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel)
  {
    Set<String> klassNatureRelationshipIds = helperModel.getKlassNatureRelationshipIds();
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    referencedElements.keySet()
        .removeAll(klassNatureRelationshipIds);
  }
  
  private void fillReferencedContextLinkedToKlass(IGetConfigDetailsHelperModel helperModel,
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
  
  private void fillTemplateDetailsForCustomTemplate(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, String requestedTabId, String requestedTemplateId)
      throws Exception
  {
    Map<String, Object> referencedTemplate = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TEMPLATE, referencedTemplate);
    List<Map<String, Object>> tabsList = new ArrayList<>();
    referencedTemplate.put(ITemplate.TABS, tabsList);
    String vertextLabelForDocument = "";
    vertextLabelForDocument = VertexLabelConstants.TEMPLATE;
    Vertex requestedTemplateVertex = UtilClass.getVertexById(requestedTemplateId,
        vertextLabelForDocument);
    Map<String, Object> requestedTypeMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, ITemplate.LABEL, ITemplate.CODE),
        requestedTemplateVertex);
    referencedTemplate.put(ITemplate.TYPE, CommonConstants.CUSTOM_TEMPLATE);
    referencedTemplate.put(ITemplate.ID, requestedTypeMap.get(ITemplate.ID));
    referencedTemplate.put(ITemplate.LABEL, requestedTypeMap.get(ITemplate.LABEL));
    referencedTemplate.put(ITemplate.CODE, requestedTypeMap.get(ITemplate.CODE));
    
    fillTabsAssociatedWithCustomTemplate(requestedTemplateVertex, tabsList, requestedTabId,
        mapToReturn, helperModel);
  }
  
  private void fillTabsAssociatedWithCustomTemplate(Vertex requestedTemplateVertex,
      List<Map<String, Object>> tabsList, String requestedTabId, Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Set<String> propertyCollectionAssociatedWithMinorTaxonomies = getPropertyCollectionIdsAssociatedWithMinorTaxonomy(
        mapToReturn);
    Set<String> tabIds = new HashSet<>();
    Iterator<Vertex> propertyCollectionIterator = requestedTemplateVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION)
        .iterator();
    List<Vertex> propertyCollectionList = IteratorUtils.toList(propertyCollectionIterator);
    fillTabIdsAssociatedWithPropertyCollection(new HashSet<>(propertyCollectionList), tabIds,
        propertyCollectionAssociatedWithMinorTaxonomies);
    fillReferencedPropertyCollectionsForCustomTemplate(propertyCollectionList, mapToReturn);
    Iterator<Vertex> natureRelationshipIterator = requestedTemplateVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP)
        .iterator();
    Iterator<Vertex> relationshipIterator = requestedTemplateVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP)
        .iterator();
    List<Vertex> relationshipVertices = IteratorUtils.toList(natureRelationshipIterator);
    relationshipVertices.addAll(IteratorUtils.toList(relationshipIterator));
    fillTabIdsAssociatedWithRelationships(new HashSet<>(relationshipVertices), tabIds);
    Iterator<Vertex> contextIterator = requestedTemplateVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_CONTEXT)
        .iterator();
    List<Vertex> contextList = IteratorUtils.toList(contextIterator);
    fillTabIdsAssociatedWithContexts(new HashSet<>(contextList), tabIds);
    
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    
    // add default runtime tabs i.e, task, event and timeline tab
    tabIds.addAll(getDefaultRuntimeTabs(helperModel));
    sequenceList.retainAll(tabIds);
    if (requestedTabId == null) {
      requestedTabId = getFirstCustomTab(sequenceList);
    }
    if (!tabIds.contains(requestedTabId)) {
      requestedTabId = getFirstCustomTab(sequenceList);
    }
    Set<String> propertySequenceListToRetain = new HashSet<>();
    Map<String, Object> referencedRelationshipMapping = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING);
    if (referencedRelationshipMapping != null) {
      propertySequenceListToRetain.addAll(new HashSet<>(referencedRelationshipMapping.keySet()));
    }
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    if (referencedVariantContexts != null) {
      Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContexts
          .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
      if (embeddedVariantContexts != null) {
        propertySequenceListToRetain.addAll(new HashSet<>(embeddedVariantContexts.keySet()));
      }
    }
    propertySequenceListToRetain.addAll(UtilClass.getCodes(propertyCollectionList));
    
    Set<String> templatePropertiesToRetain = new HashSet<>();
    templatePropertiesToRetain.addAll(UtilClass.getCodes(relationshipVertices));
    templatePropertiesToRetain.addAll(UtilClass.getCodes(propertyCollectionList));
    templatePropertiesToRetain.addAll(UtilClass.getCodes(contextList));
    propertySequenceListToRetain.retainAll(templatePropertiesToRetain);
    
    for (String tabId : sequenceList) {
      Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      List<String> propertySequenceList = (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST);
      if (tabId.equals(SystemLevelIds.OVERVIEW_TAB)) {
        propertySequenceList.addAll(helperModel.getNatureRelationshipIds());
      }
      else {
        propertySequenceList.removeAll(helperModel.getNatureRelationshipIds());
      }
      propertySequenceList.retainAll(propertySequenceListToRetain);
      tabMap.put(ITemplateTab.BASE_TYPE, CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE);
      tabsList.add(tabMap);
      if (tabId.equals(requestedTabId)) {
        tabMap.put(ITemplateTab.IS_SELECTED, true);
        filterReferencedDataAccordingToRequestedTabForCustomTemplate(mapToReturn, tabMap,
            helperModel);
      }
    }
  }
  
  private Set<String> getPropertyCollectionIdsAssociatedWithMinorTaxonomy(
      Map<String, Object> mapToReturn)
  {
    Set<String> propertyCollectionIdsAssociatedWithKlasses = new HashSet<>();
    Map<String, Object> referencedKlasses = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    referencedKlasses.forEach((klassId, referencedKlass) -> {
      List<String> propertyCollectionIds = (List<String>) ((Map<String, Object>) referencedKlass)
          .get(IReferencedKlassDetailStrategyModel.PROPERTY_COLLECTIONS);
      // property Collection ids are null for embedded type in referenced klass
      if (propertyCollectionIds != null) {
        propertyCollectionIdsAssociatedWithKlasses.addAll(propertyCollectionIds);
      }
    });
    
    Set<String> propertyCollectionAssociatedWithMinorTaxonomies = new HashSet<>();
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    referencedTaxonomies.forEach((taxonomyId, taxonomyMap) -> {
      Map<String, Object> rootLevelParent = new HashMap<>((Map<String, Object>) taxonomyMap);
      Map<String, Object> parent = (Map<String, Object>) (rootLevelParent)
          .get(IReferencedArticleTaxonomyModel.PARENT);
      while (!(parent.get(IReferencedTaxonomyParentModel.ID)).equals("-1")) {
        rootLevelParent.clear();
        rootLevelParent.putAll(parent);
        parent = (Map<String, Object>) (parent).get(IReferencedArticleTaxonomyModel.PARENT);
      }
      String taxonomyType = (String) rootLevelParent
          .get(IReferencedArticleTaxonomyModel.TAXONOMY_TYPE);
      if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
        propertyCollectionAssociatedWithMinorTaxonomies
            .addAll((List<String>) ((Map<String, Object>) taxonomyMap)
                .get(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS));
      }
    });
    propertyCollectionAssociatedWithMinorTaxonomies
        .removeAll(propertyCollectionIdsAssociatedWithKlasses);
    return propertyCollectionAssociatedWithMinorTaxonomies;
  }
  
  private void filterReferencedDataAccordingToRequestedTabForCustomTemplate(
      Map<String, Object> mapToReturn, Map<String, Object> tabMap,
      IGetConfigDetailsHelperModel helperModel)
  {
    List<String> propertySequenceList = new ArrayList<>(
        (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST));
    Map<String, Object> referencedNatureRelationships = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS);
    referencedNatureRelationships.keySet()
        .retainAll(propertySequenceList);
    Map<String, Object> referencedRelationship = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS);
    referencedRelationship.keySet()
        .retainAll(propertySequenceList);
    Map<String, Object> referencedVariantContext = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContext
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    propertySequenceList.addAll(helperModel.getAssociatedAttributeContextIds());
    embeddedVariantContexts.keySet()
        .retainAll(propertySequenceList);
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
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS,
        referencedRelationshipMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_TAGS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES, new ArrayList<>());
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS, new ArrayList<>());
    return mapToReturn;
  }
  
  protected void fillTemplateDetails(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, String requestedTabId, String requestedTypeId,
      Boolean isGridEditable) throws Exception
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
          Arrays.asList(CommonConstants.CODE_PROPERTY, ITemplate.LABEL, ITemplate.CODE),
          requestedTypeVertex);
      referencedTemplate.put(ITemplate.TYPE, CommonConstants.CUSTOM_TEMPLATE);
      referencedTemplate.put(ITemplate.ID, requestedTypeMap.get(ITemplate.ID));
      referencedTemplate.put(ITemplate.LABEL, requestedTypeMap.get(ITemplate.LABEL));
      referencedTemplate.put(ITemplate.CODE, requestedTypeMap.get(ITemplate.CODE));
      fillTabIdsAssociatedWithRequestedType(helperModel, tabIds, mapToReturn);
    }
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    
    // add default runtime tabs i.e, task, event and timeline tab
    tabIds.addAll(getDefaultRuntimeTabs(helperModel));
    sequenceList.retainAll(tabIds);
    if (requestedTabId == null) {
      requestedTabId = getFirstCustomTab(sequenceList);
    }
    if (!tabIds.contains(requestedTabId)) {
      requestedTabId = getFirstCustomTab(sequenceList);
    }
    
    for (String tabId : sequenceList) {
      Vertex tabNode = UtilClass.getVertexByIndexedId(tabId, VertexLabelConstants.TAB);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      List<String> propertySequenceList = (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST);
      propertySequenceList.retainAll(helperModel.getAssociatedSectionIds());
      // Remove nature relationships
      Set<String> natureRelationshipIds = new HashSet<>(helperModel.getNatureRelationshipIds());
      natureRelationshipIds.removeAll(helperModel.getRelationshipIds());
      Set<String> relationshipIds = new HashSet<>(helperModel.getRelationshipIds());
      relationshipIds.retainAll(helperModel.getNatureRelationshipIds());
      propertySequenceList.removeAll(natureRelationshipIds);
      tabMap.put(ITemplateTab.BASE_TYPE, getTabTypeById(tabId));
      tabsList.add(tabMap);
      if (tabId.equals(requestedTabId) && !isGridEditable) {
        tabMap.put(ITemplateTab.IS_SELECTED, true);
        filterReferencedDataAccordingToRequestedTab(mapToReturn, tabMap, helperModel,
            relationshipIds);
      }
    }
  }
  
  private String getFirstCustomTab(List<String> sequenceList)
  {
    List<String> cloneSequenceList = new ArrayList<>(sequenceList);
    cloneSequenceList.removeAll(IStandardConfig.StandardTab.DefaultRuntimeTabs);
    if (cloneSequenceList.isEmpty()) {
      return null;
    }
    return cloneSequenceList.get(0);
  }
  
  protected void filterReferencedDataAccordingToRequestedTab(Map<String, Object> mapToReturn,
      Map<String, Object> tabMap, IGetConfigDetailsHelperModel helperModel,
      Set<String> relationshipIds)
  {
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Set<String> propertyCollectionAssociatedWithMinorTaxonomies = new HashSet<>();
    referencedTaxonomies.forEach((taxonomyId, taxonomyMap) -> {
      Map<String, Object> rootLevelParent = new HashMap<>((Map<String, Object>) taxonomyMap);
      Map<String, Object> parent = (Map<String, Object>) (rootLevelParent)
          .get(IReferencedArticleTaxonomyModel.PARENT);
      while (!(parent.get(IReferencedTaxonomyParentModel.ID)).equals("-1")) {
        rootLevelParent.clear();
        rootLevelParent.putAll(parent);
        parent = (Map<String, Object>) (parent).get(IReferencedArticleTaxonomyModel.PARENT);
      }
      String taxonomyType = (String) rootLevelParent
          .get(IReferencedArticleTaxonomyModel.TAXONOMY_TYPE);
      if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
        propertyCollectionAssociatedWithMinorTaxonomies
            .addAll((List<String>) ((Map<String, Object>) taxonomyMap)
                .get(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS));
      }
    });
    Set<String> propertySequenceList = new HashSet<>(
        (List<String>) tabMap.get(ITab.PROPERTY_SEQUENCE_LIST));
    propertySequenceList.addAll(propertyCollectionAssociatedWithMinorTaxonomies);
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    referencedPropertyCollections.keySet()
        .retainAll(propertySequenceList);
    Map<String, Object> referencedNatureRelationships = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS);
    referencedNatureRelationships.keySet()
        .retainAll(propertySequenceList);
    referencedNatureRelationships.keySet()
        .removeAll(relationshipIds);
    Map<String, Object> referencedRelationship = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS);
    referencedRelationship.keySet()
        .retainAll(propertySequenceList);
    Map<String, Object> referencedVariantContext = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContext
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    propertySequenceList.addAll(helperModel.getAssociatedAttributeContextIds());
    embeddedVariantContexts.keySet()
        .retainAll(propertySequenceList);
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
