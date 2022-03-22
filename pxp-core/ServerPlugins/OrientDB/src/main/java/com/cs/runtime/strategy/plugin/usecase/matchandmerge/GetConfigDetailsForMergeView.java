package com.cs.runtime.strategy.plugin.usecase.matchandmerge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.matchandmerge.IGetConfigDetailsForMatchAndMergeModel;
import com.cs.core.config.interactor.model.matchandmerge.IMatchAndMergeConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForCustomTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForMergeView extends AbstractConfigDetailsForCustomTab {
  
  public GetConfigDetailsForMergeView(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForMergeView/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    helperModel.setMatchRelationshipTypes((List<String>) requestMap
        .get(IMatchAndMergeConfigDetailsRequestModel.MATCH_RELATIONSHIP_TYPES));
    
    // no tag values in match and merge view so need to get all files.
    helperModel.setShouldUseTagIdTagValueIdsMap(false);
    
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    Map<String, List<String>> tagIdTagValueIdsMap = (Map<String, List<String>>) requestMap
        .get(IMulticlassificationRequestModel.TAG_ID_TAG_VALUE_IDS_MAP);
    helperModel.setTagIdTagValueIdsMap(tagIdTagValueIdsMap);
    
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
    
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    filterOutReferencedElements(mapToReturn, helperModel);
    filterOutReferencedElementsForNatureRelationship(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    fillReferencedLanguages(mapToReturn,
        (List<String>) requestMap.get(IMulticlassificationRequestModel.LANGUAGE_CODES));
    fillTargetTypes(mapToReturn, helperModel, requestMap);
    return mapToReturn;
  }
  
  private void fillTargetTypes(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> requestMap)
  {
    Map<String, String> relationshipIdVsTargetType = helperModel.getRelationshipIdVsTargetType();
    
    List<String> relationshipIds = (List<String>) requestMap
        .get(IMatchAndMergeConfigDetailsRequestModel.MATCH_RELATIONSHIP_IDS);
    List<String> matchRelationshipTypes = helperModel.getMatchRelationshipTypes();
    if (matchRelationshipTypes.isEmpty()) {
      relationshipIdVsTargetType.keySet()
          .retainAll(relationshipIds);
    }
    
    HashSet<String> targetTypes = new HashSet<>(relationshipIdVsTargetType.values());
    
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.TARGET_TYPES,
        new ArrayList<>(targetTypes));
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.MATCH_RELATIONSHIP_IDS,
        relationshipIdVsTargetType.keySet());
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
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    
    Map<String, Object> mapToReturn = super.getMapToReturn(referencedDataRuleMap);
    
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.REFERENCED_TEMPLATE, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.REFERENCED_NATURE_RELATIONSHIPS,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.REFERENCED_RELATIONSHIPS,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.TARGET_TYPES, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.REFERENCED_PROPERTY_COLLECTIONS,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.MATCH_RELATIONSHIP_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.REFERENCED_RELATIONSHIPS_MAPPING,
        new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.VERSIONABLE_ATTRIBUTES,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.VERSIONABLE_TAGS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.MANDATORY_ATTRIBUTE_IDS,
        new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForMatchAndMergeModel.SHOULD_ATTRIBUTE_IDS, new ArrayList<>());
    return mapToReturn;
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
  }
  
  protected void fillKlassDetails(Map<String, Object> mapToReturn, List<String> klassIds,
      Map<String, Object> referencedDataRuleMap, IGetConfigDetailsHelperModel helperModel,
      String requestedTypeId, String roleId) throws Exception
  {
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    for (String klassId : klassIds) {
      try {
        Vertex klassVertex = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        if (klassId.equals(requestedTypeId)) {
          helperModel.setRequestedTypeVertex(klassVertex);
          helperModel.setRequestedTypeVertexLabelInfo(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
            IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
            IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.PREVIEW_IMAGE, IKlass.CODE);
        
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
        helperModel.setKlassType(klassVertex.getProperty(IKlass.TYPE));
        Integer numberOfVersionsToMaintain = (Integer) klassMap
            .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
            .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
        if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
          mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
              numberOfVersionsToMaintain);
        }
        fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
        fillReferencedElements(mapToReturn, klassVertex,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, null, helperModel, null);
        fillReferencedPropertyCollections(helperModel, klassVertex, mapToReturn, null);
        
        klassMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
            UtilClass.getCodes(
                new ArrayList<>(helperModel.getTypeIdVsAssociatedPropertyCollectionVertices()
                    .get(klassId))));
        Boolean isNature = klassVertex.getProperty(IKlass.IS_NATURE);
        if (isNature != null && isNature) {
          helperModel.setNatureNode(klassVertex);
          fillReferencedNatureRelationships(mapToReturn, klassVertex, helperModel);
        }
        else {
          helperModel.getNonNatureKlassNodes()
              .add(klassVertex);
        }
        referencedKlassMap.put(klassId, klassMap);
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
        taxonomyVertex = UtilClass.getVertexById(taxonomyId,
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
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE),
          taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      fillReferencedElements(mapToReturn, taxonomyVertex, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
          null, helperModel, null);
      fillReferencedPropertyCollections(helperModel, taxonomyVertex, mapToReturn, null);
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
          UtilClass.getCodes(
              new ArrayList<>(helperModel.getTypeIdVsAssociatedPropertyCollectionVertices()
                  .get(taxonomyId))));
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
    }
    helperModel.setTaxonomyVertices(taxonomyVertices);
  }
  
  protected void fillRelationships(Vertex klassVertex, Map<String, Object> mapToReturn,
      Vertex klassRelationshipNode, Map<String, Object> referencedElementMap,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> entitiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP);
    
    Iterator<Vertex> relationshipIterator = klassRelationshipNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex relationshipNode = relationshipIterator.next();
    String typeId = UtilClass.getCodeNew(klassVertex);
    
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    helperModel.getRelationshipIds()
        .add(relationshipId);
    
    Map<String, Object> side = (Map<String, Object>) referencedElementMap
        .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
    String targetKlassType = (String) side.get(IKlassRelationshipSide.TARGET_TYPE);
    String moduleEntity = EntityUtil.getModuleEntityFromKlassType(targetKlassType);
    
    if (!entitiesHavingReadPermission.contains(moduleEntity) && moduleEntity != null) {
      return;
    }
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipsVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    if (typeIdVsAssociatedRelationshipsVertices.containsKey(typeId)) {
      typeIdVsAssociatedRelationshipsVertices.get(typeId)
          .add(relationshipNode);
    }
    else {
      Set<Vertex> associatedRelationshipVertices = new HashSet<>();
      associatedRelationshipVertices.add(relationshipNode);
      typeIdVsAssociatedRelationshipsVertices.put(typeId, associatedRelationshipVertices);
    }
    
    String klassRelationshipId = UtilClass.getCodeNew(klassRelationshipNode);
    helperModel.getKlassRelationshipIds()
        .add(klassRelationshipId);
    referencedElements.put(klassRelationshipId, referencedElementMap);
    
    if (side != null) {
      side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, klassRelationshipId);
      String contextId = RelationshipUtils.getContextId(klassRelationshipNode);
      side.put(IRelationshipSide.CONTEXT_ID, contextId);
    }
    
    if (helperModel.getMatchRelationshipTypes()
        .isEmpty()) {
      // added for filtering target types of matched relationship in
      // match-and-merge
      Map<String, String> relationshipIdVsTargetType = helperModel.getRelationshipIdVsTargetType();
      relationshipIdVsTargetType.put(relationshipId, targetKlassType);
    }
  }
  
  protected void fillReferencedNatureRelationships(Map<String, Object> mapToReturn,
      Vertex klassNode, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    List<String> matchRelationshipTypes = helperModel.getMatchRelationshipTypes();
    Map<String, Object> referencedElementMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    
    Iterable<Edge> klassNatureRelationshipOfEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    String klassId = UtilClass.getCodeNew(klassNode);
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipsVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<Vertex> associatedRelationshipVertices = new HashSet<>();
    if (typeIdVsAssociatedRelationshipsVertices.containsKey(klassId)) {
      associatedRelationshipVertices = typeIdVsAssociatedRelationshipsVertices.get(klassId);
    }
    else {
      typeIdVsAssociatedRelationshipsVertices.put(klassId, associatedRelationshipVertices);
    }
    for (Edge klassNatureRelationshipOfEdge : klassNatureRelationshipOfEdges) {
      Vertex klassNatureRelationshipVertex = klassNatureRelationshipOfEdge.getVertex(Direction.IN);
      Iterable<Vertex> natureRelationshipNodes = klassNatureRelationshipVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex natureRelationshipNode : natureRelationshipNodes) {
        
        associatedRelationshipVertices.add(natureRelationshipNode);
        String natureRelationshipId = UtilClass.getCodeNew(natureRelationshipNode);
        
        // Adding Referenced Element
        Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
            klassNatureRelationshipVertex);
        String klassNatureRelationshipId = UtilClass.getCodeNew(klassNatureRelationshipVertex);
        helperModel.getKlassNatureRelationshipIds()
            .add(klassNatureRelationshipId);
        referencedElement.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        referencedElement.put(IReferencedSectionRelationshipModel.ID, klassNatureRelationshipId);
        referencedElement.put(IReferencedSectionRelationshipModel.IS_DISABLED, false);
        if (typeIdVsAssociatedPropertyIds.containsKey(klassId)) {
          Set<String> associatedPropertyIds = typeIdVsAssociatedPropertyIds.get(klassId);
          associatedPropertyIds.add(natureRelationshipId);
        }
        else {
          Set<String> associatedPropertyIds = new HashSet<>();
          associatedPropertyIds.add(natureRelationshipId);
          typeIdVsAssociatedPropertyIds.put(klassId, associatedPropertyIds);
        }
        Map<String, Object> side = (Map<String, Object>) referencedElement
            .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
        GetRelationshipUtils.prepareSideMapTranslation(side);
        
        // Adding Referenced Nature Relationship
        if (side != null) {
          side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, klassNatureRelationshipId);
          String contextId = RelationshipUtils
              .getContextIdForNatureRelationship(klassNatureRelationshipVertex);
          side.put(IRelationshipSide.CONTEXT_ID, contextId);
        }
        helperModel.getNatureRelationshipIds()
            .add(natureRelationshipId);
        referencedElementMap.put(klassNatureRelationshipId, referencedElement);
        
        if (!matchRelationshipTypes.isEmpty()) {
          HashMap<String, Object> natureRelationshipMap = RelationshipUtils
              .getNatureRelationshipMap(natureRelationshipNode);
          String relationshipType = (String) natureRelationshipMap.get(IRelationship.TYPE);
          if (!matchRelationshipTypes.contains(relationshipType)) {
            continue;
          }
        }
        // added for filtering target types of matched relationships in
        // match-and-merge
        String targetTypeKlass = (String) side.get(IKlassRelationshipSide.TARGET_TYPE);
        helperModel.getRelationshipIdVsTargetType()
            .put(natureRelationshipId, targetTypeKlass);
      }
    }
  }
}
