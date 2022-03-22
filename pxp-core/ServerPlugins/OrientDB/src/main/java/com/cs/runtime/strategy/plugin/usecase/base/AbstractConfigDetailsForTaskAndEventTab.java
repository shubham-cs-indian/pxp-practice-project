package com.cs.runtime.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigDetailsForTaskAndEventTab
    extends AbstractConfigDetailsForCustomTab {
  
  public AbstractConfigDetailsForTaskAndEventTab(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void fillKlassDetails(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId, String roleId) throws Exception
  {
    List<Map<String, Object>> referencedTemplates = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (klassId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(klassVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        helperModel.setKlassType(klassVertex.getProperty(IKlass.TYPE));
        if (isNature != null && isNature) {
          helperModel.setNatureNode(klassVertex);
        }
        else {
          helperModel.getNonNatureKlassNodes()
              .add(klassVertex);
        }
        
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.PREVIEW_IMAGE, IKlass.CODE);
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
        fillReferencedTasks(klassVertex, mapToReturn);
        fillPropertyCollectionsForKlass(helperModel, klassVertex);
        fillEmbeddedContextForKlass(klassVertex, helperModel);
        fillRelationshipsForKlass(helperModel, klassVertex);
        referencedKlassMap.put(klassId, klassMap);
        if (isNature != null && isNature) {
          fillNatureRelationshipForKlass(helperModel, klassVertex);
        }
        fillReferencedTemplates(referencedTemplates, roleId, klassId, referencedPermissions);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        if (taxonomyId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(taxonomyVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        }
        taxonomyVertices.add(taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE),
          taxonomyVertex);
      
      Iterable<Vertex> sectionVertices = taxonomyVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      List<Map<String, Object>> propertyCollection = new ArrayList<>();
      for (Vertex sectionVertex : sectionVertices) {
        Iterable<Vertex> propertyCollectionVertices = sectionVertex.getVertices(Direction.OUT,
            RelationshipLabelConstants.PROPERTY_COLLECTION_OF);
        for (Vertex propertyCollectionVertex : propertyCollectionVertices) {
          Map<String, Object> temporaryMap = new HashMap<>();
          temporaryMap.put(IIdParameterModel.ID, UtilClass.getCodeNew(propertyCollectionVertex));
          propertyCollection.add(temporaryMap);
        }
      }
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS, propertyCollection);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      fillPropertyCollectionsForKlass(helperModel, taxonomyVertex);
      fillEmbeddedContextForKlass(taxonomyVertex, helperModel);
      fillRelationshipsForKlass(helperModel, taxonomyVertex);
      fillReferencedTasks(taxonomyVertex, mapToReturn);
    }
    helperModel.setTaxonomyVertices(taxonomyVertices);
  }
  
  protected void fillCollectionDetails(List<String> collectionIds,
      IGetConfigDetailsHelperModel helperModel, String requestedTypeId) throws Exception
  {
    Set<Vertex> collectionVertices = helperModel.getCollectionVertices();
    for (String collectionId : collectionIds) {
      try {
        Vertex collectionVertex = UtilClass.getVertexById(collectionId,
            VertexLabelConstants.COLLECTION);
        if (collectionId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(collectionVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.COLLECTION);
        }
        collectionVertices.add(collectionVertex);
        fillPropertyCollectionsForKlass(helperModel, collectionVertex);
      }
      catch (NotFoundException ex) {
        throw new CollectionNodeNotFoundException(ex);
      }
    }
  }
  
  protected void fillPropertyCollectionsForKlass(IGetConfigDetailsHelperModel helperModel,
      Vertex klassVertex)
  {
    String typeId = UtilClass.getCodeNew(klassVertex);
    Set<Vertex> associatedPropertyCollectionVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    if (typeIdVsAssociatedPropertyCollectionVertices.containsKey(typeId)) {
      associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
          .get(typeId);
    }
    else {
      typeIdVsAssociatedPropertyCollectionVertices.put(typeId,
          associatedPropertyCollectionVertices);
    }
    Iterable<Vertex> sectionVertices = klassVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex sectionVertex : sectionVertices) {
      Iterable<Vertex> propertyCollectionVertices = sectionVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.PROPERTY_COLLECTION_OF);
      for (Vertex propertyCollectionVertex : propertyCollectionVertices) {
        associatedPropertyCollectionVertices.add(propertyCollectionVertex);
      }
    }
  }
  
  protected void fillRelationshipsForKlass(IGetConfigDetailsHelperModel helperModel,
      Vertex klassVertex)
  {
    String typeId = UtilClass.getCodeNew(klassVertex);
    Set<Vertex> associatedRelationshipVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    if (typeIdVsAssociatedRelationshipVertices.containsKey(typeId)) {
      associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices.get(typeId);
    }
    else {
      typeIdVsAssociatedRelationshipVertices.put(typeId, associatedRelationshipVertices);
    }
    
    String query = "select from " + VertexLabelConstants.ROOT_RELATIONSHIP + " where in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "').in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code contains '" + typeId + "'";
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex relationshipNode : resultIterable) {
      associatedRelationshipVertices.add(relationshipNode);
    }
  }
  
  protected void fillNatureRelationshipForKlass(IGetConfigDetailsHelperModel helperModel,
      Vertex klassVertex)
  {
    String typeId = UtilClass.getCodeNew(klassVertex);
    Set<Vertex> associatedRelationshipVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    if (typeIdVsAssociatedRelationshipVertices.containsKey(typeId)) {
      associatedRelationshipVertices = typeIdVsAssociatedRelationshipVertices.get(typeId);
    }
    else {
      typeIdVsAssociatedRelationshipVertices.put(typeId, associatedRelationshipVertices);
    }
    
    Iterable<Vertex> resultIterable = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    
    for (Vertex kNRNode : resultIterable) {
      Iterator<Vertex> iterator = kNRNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex natureRelationshipNode = iterator.next();
      associatedRelationshipVertices.add(natureRelationshipNode);
      helperModel.getNatureRelationshipIds()
          .add(UtilClass.getCodeNew(natureRelationshipNode));
    }
  }
  
  protected void fillEmbeddedContextForKlass(Vertex klassVertex,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    String klassType = helperModel.getKlassType();
    Set<Vertex> associatedContextVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedContextVertices = helperModel
        .getTypeIdVsAssociatedContextVertices();
    String klassId = UtilClass.getCodeNew(klassVertex);
    if (typeIdVsAssociatedContextVertices.containsKey(klassId)) {
      associatedContextVertices = typeIdVsAssociatedContextVertices.get(klassId);
    }
    else {
      typeIdVsAssociatedContextVertices.put(klassId, associatedContextVertices);
    }
    Iterable<Vertex> contextKlassIterable = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      String contextKlassType = contextKlassNode.getProperty(IKlass.TYPE);
      if (!contextKlassType.equals(klassType)) {
        continue;
      }
      Iterator<Vertex> variantContextsIterator = contextKlassNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      Vertex variantContextNode = variantContextsIterator.next();
      helperModel.getKlassIdVsContextId()
          .put(UtilClass.getCodeNew(contextKlassNode), UtilClass.getCodeNew(variantContextNode));
      associatedContextVertices.add(variantContextNode);
    }
  }
  
  protected void fillTemplateDetails(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, String requestedTypeId, String requestedTabId)
      throws Exception
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
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequenceList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    
    // add default runtime tabs i.e, task, event and timeline tab
    tabIds.addAll(getDefaultRuntimeTabs(helperModel));
    sequenceList.retainAll(tabIds);
    for (String tabId : sequenceList) {
      Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      tabMap.put(ITemplateTab.BASE_TYPE, getTabTypeById(tabId));
      tabsList.add(tabMap);
      if (tabId.equals(requestedTabId)) {
        tabMap.put(ITemplateTab.IS_SELECTED, true);
      }
    }
  }
  
  protected void fillTemplateDetailsForCustomTemplate(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, String requestedTabId, String requestedTemplateId)
      throws Exception
  {
    Map<String, Object> referencedTemplate = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TEMPLATE, referencedTemplate);
    List<Map<String, Object>> tabsList = new ArrayList<>();
    referencedTemplate.put(ITemplate.TABS, tabsList);
    Vertex requestedTemplateVertex = UtilClass.getVertexById(requestedTemplateId,
        VertexLabelConstants.TEMPLATE);
    Map<String, Object> requestedTypeMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, ITemplate.LABEL), requestedTemplateVertex);
    referencedTemplate.put(ITemplate.TYPE, CommonConstants.CUSTOM_TEMPLATE);
    referencedTemplate.put(ITemplate.ID, requestedTypeMap.get(ITemplate.ID));
    referencedTemplate.put(ITemplate.LABEL, requestedTypeMap.get(ITemplate.LABEL));
    fillTabsAssociatedWithCustomTemplate(requestedTemplateVertex, tabsList, requestedTabId,
        mapToReturn, helperModel);
  }
  
  private void fillTabsAssociatedWithCustomTemplate(Vertex requestedTemplateVertex,
      List<Map<String, Object>> tabsList, String requestedTabId, Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Set<String> tabIds = new HashSet<>();
    Iterator<Vertex> propertyCollectionIterator = requestedTemplateVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION)
        .iterator();
    List<Vertex> propertyCollectionList = IteratorUtils.toList(propertyCollectionIterator);
    fillTabIdsAssociatedWithPropertyCollection(new HashSet<>(propertyCollectionList), tabIds);
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
    
    for (String tabId : sequenceList) {
      Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
      tabMap.put(ITemplateTab.BASE_TYPE, getTabTypeById(tabId));
      tabsList.add(tabMap);
      if (tabId.equals(requestedTabId)) {
        tabMap.put(ITemplateTab.IS_SELECTED, true);
      }
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
