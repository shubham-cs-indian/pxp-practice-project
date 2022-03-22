package com.cs.config.strategy.plugin.usecase.relationshipInheritance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipConflictingSource;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IRelationshipInheritanceOnTypeSwitchRequestModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForRelationshipInheritanceOnTypeSwitch extends AbstractOrientPlugin {
  
  public GetConfigDetailsForRelationshipInheritanceOnTypeSwitch(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForRelationshipInheritanceOnTypeSwitch/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Set<String> allRelationshipSidesToBeRemoved = new HashSet<>();
    Set<String> notApplicableNatureSideIds = new HashSet<>();
    Set<String> newSideIds = getNewlyApplicableSideIdsAndFillSideIdsToBeRemoved(requestMap,
        allRelationshipSidesToBeRemoved, notApplicableNatureSideIds);
    
    Map<String, String> natureRelIdSideIdMap = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> configuredRelSidesPerNatureRelSides = new HashMap<>();
    Map<String, String> natureRelSideIdsToInherit = new HashMap<>();
    
    List<String> existingKlassIds = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.EXISTING_TYPES);
    Set<String> notApplicableRelIdSideId = new HashSet<>();
    for (String klassId : existingKlassIds) {
      Vertex klass = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
      Boolean isNature = klass.getProperty(IKlass.IS_NATURE);
      if (isNature) {
        notApplicableRelIdSideId = fillConfiguredRelationshipsFromNatureKlass(newSideIds,
            natureRelIdSideIdMap, referencedRelationships, configuredRelSidesPerNatureRelSides,
            klass.getId(), allRelationshipSidesToBeRemoved);
        break;
      }
    }
    fillConfiguredRelationshipsFromKlasses(newSideIds, natureRelSideIdsToInherit,
        referencedRelationships, configuredRelSidesPerNatureRelSides, existingKlassIds);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.NATURE_REL_ID_SIDE_ID,
        natureRelIdSideIdMap);
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.CONIGURED_REL_SIDES_PER_NATURE_REL_SIDES,
        configuredRelSidesPerNatureRelSides);
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.REFERENCED_RELATIONSHIPS,
        referencedRelationships);
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.NATURE_REL_SIDE_IDS_TO_INHERIT,
        natureRelSideIdsToInherit);
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.NOT_APPLICABLE_RELID_SIDEIDS,
        notApplicableRelIdSideId);
    returnMap.put(
        IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel.NOT_APPLICABLE_NATURE_SIDEIDS,
        notApplicableNatureSideIds);
    return returnMap;
  }
  
  private Set<String> fillConfiguredRelationshipsFromNatureKlass(Set<String> newSideIds,
      Map<String, String> natureRelIdSideIdMap, Map<String, Object> referencedRelationships,
      Map<String, Object> configuredRelSidesPerNatureRelSides, Object klassRid,
      Set<String> allRelationshipSidesToBeRemoved) throws Exception
  {
    Set<String> notApplicableRelIdSideId = new HashSet<>();
    String natureRelationshipKRQuery = "select expand(out('Klass_Nature_Relationship_Of')[relationshipType in "
        + EntityUtil.quoteIt(CommonConstants.RELATIONSHIP_TYPES_TO_TRANSFER) + " ]) from "
        + klassRid;
    Iterable<Vertex> natureKRNodes = UtilClass.getGraph()
        .command(new OCommandSQL(natureRelationshipKRQuery))
        .execute();
    for (Vertex natureKRNode : natureKRNodes) {
      Iterable<Edge> inheritanceRelationshipEdges = natureKRNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
      String natureRelationshipId = natureKRNode.getProperty(CommonConstants.PROPERTY_ID);
      String natureRelationshipSideId = UtilClass.getCodeNew(natureKRNode);
      for (Edge inheritanceRelationshipEdge : inheritanceRelationshipEdges) {
        Vertex relationshipNode = inheritanceRelationshipEdge.getVertex(Direction.IN);
        fillConfiguredRelationshipInfo(newSideIds, referencedRelationships,
            configuredRelSidesPerNatureRelSides, natureRelationshipId, natureRelationshipSideId,
            inheritanceRelationshipEdge
                .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE),
            relationshipNode, allRelationshipSidesToBeRemoved, notApplicableRelIdSideId);
        natureRelIdSideIdMap.put(natureRelationshipId, natureRelationshipSideId);
      }
    }
    
    return notApplicableRelIdSideId;
  }
  
  private void fillConfiguredRelationshipsFromKlasses(Set<String> newSideIds, Map<String, String> natureRelSideIdsToInherit,
      Map<String, Object> referencedRelationships, Map<String, Object> configuredRelSidesPerNatureRelSides, List<String> klassIds) throws Exception
  {
    Iterable<Vertex> natureKRNodes = RelationshipRepository
        .getOtherSideEligibleNatureKRNodesFromKlassIds(klassIds);
    for (Vertex natureKRNode : natureKRNodes) {
      Iterable<Edge> inheritanceRelationshipEdges = natureKRNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
      String natureRelationshipId = natureKRNode.getProperty(CommonConstants.PROPERTY_ID);
      String natureRelationshipSideId = UtilClass.getCodeNew(natureKRNode);
      if (configuredRelSidesPerNatureRelSides.containsKey(natureRelationshipId
          + Seperators.RELATIONSHIP_SIDE_SPERATOR + natureRelationshipSideId)) {
        natureRelSideIdsToInherit.put(natureRelationshipId,natureRelationshipSideId);
        continue;
      }
      for (Edge inheritanceRelationshipEdge : inheritanceRelationshipEdges) {
        Vertex relationshipNode = inheritanceRelationshipEdge.getVertex(Direction.IN);
        fillConfiguredRelationshipInfo(newSideIds, referencedRelationships,
            configuredRelSidesPerNatureRelSides, natureRelationshipId, natureRelationshipSideId,
            inheritanceRelationshipEdge
                .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE),
            relationshipNode, new HashSet<>(), new HashSet<>());
        natureRelSideIdsToInherit.put(natureRelationshipId,natureRelationshipSideId);
      }
    }
  }
  
  private void fillConfiguredRelationshipInfo(Set<String> newSideIds,
      Map<String, Object> referencedRelationships,
      Map<String, Object> configuredRelSidesPerNatureRelSides, String natureRelationshipId,
      String natureRelationshipSideId, String couplingType, Vertex relationshipNode,
      Set<String> allRelationshipSidesToBeRemoved, Set<String> notApplicableRelIdSideId)
      throws Exception
  {
    Iterable<Vertex> sides = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    for (Vertex side : sides) {
      String cardinality = (String) ((Map<String, Object>) side
          .getProperty(CommonConstants.RELATIONSHIP_SIDE)).get("sourceCardinality");
      String sideId = UtilClass.getCodeNew(side);
      
      if (allRelationshipSidesToBeRemoved.contains(sideId)
          && !cardinality.equals(CommonConstants.CARDINALITY_ONE)) {
        notApplicableRelIdSideId
            .add(relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR + sideId);
        if (!referencedRelationships.containsKey(relationshipId)) {
          RelationshipUtils.getRelationshipMapForRelationshipInheritance(referencedRelationships,
              relationshipId);
        }
      }
      
      if (!newSideIds.contains(sideId) || cardinality.equals(CommonConstants.CARDINALITY_ONE)) {
        continue;
      }
      String nRelIdSideId = natureRelationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR
          + natureRelationshipSideId;
      Map<String, Object> configuredRels = (Map<String, Object>) configuredRelSidesPerNatureRelSides
          .get(nRelIdSideId);
      if (configuredRels == null) {
        configuredRels = new HashMap<>();
        configuredRelSidesPerNatureRelSides.put(nRelIdSideId, configuredRels);
      }
      Map<String, String> configuredRelationshipInfo = new HashMap<>();
      configuredRelationshipInfo.put(IRelationshipConflictingSource.RELATIONSHIP_ID,
          relationshipId);
      configuredRelationshipInfo.put(IRelationshipConflictingSource.RELATIONHSIP_SIDE_ID,
          sideId);
      configuredRelationshipInfo.put(IRelationshipConflictingSource.COUPLING_TYPE,
          couplingType);
      configuredRels.put(relationshipId + Seperators.RELATIONSHIP_SIDE_SPERATOR + sideId,
          configuredRelationshipInfo);
      if (!referencedRelationships.containsKey(relationshipId)) {
        RelationshipUtils.getRelationshipMapForRelationshipInheritance(referencedRelationships,
            relationshipId);
      }
    }
  }
  
  private Set<String> getNewlyApplicableSideIdsAndFillSideIdsToBeRemoved(
      Map<String, Object> requestMap, Set<String> allRelationshipSidesToBeRemoved,
      Set<String> notApplicableNatureSideIds)
  {
    
    List<String> existingKlassIds = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.EXISTING_TYPES);
    List<String> existingTaxonomyIds = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.EXISTING_TAXONOMIES);
    List<String> addedKlassIds = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.ADDED_TYPES);
    List<String> addedTaxonomyIds = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.ADDED_TAXONOMIES);
    List<String> removedTypes = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.REMOVED_TYPES);
    List<String> removedTaxonomies = (List<String>) requestMap
        .get(IRelationshipInheritanceOnTypeSwitchRequestModel.REMOVED_TAXONOMIES);
    
    Set<String> existingSideIds = new HashSet<>();
    
    if (!removedTypes.isEmpty()) {
      String query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
          + "(select from klass where code in " + EntityUtil.quoteIt(removedTypes) + " )";
      Iterable<Vertex> krNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      fillExistingRelSidemap(krNodes, allRelationshipSidesToBeRemoved);
      existingKlassIds.removeAll(removedTypes);
      
      Set<String> existingKnr = new HashSet<>();
      Iterable<Vertex> eligibleNatureKRNodesFromKlassIds = RelationshipRepository
          .getOtherSideEligibleNatureKRNodesFromKlassIds(existingKlassIds);
      fillExistingRelSidemap(eligibleNatureKRNodesFromKlassIds, existingKnr);
      // eligibleNatureKRNodesFromKlassIds =
      // RelationshipRepository.getOtherSideEligibleNatureKRNodesFromKlassIds(Arrays.asList(removedTypes));
      eligibleNatureKRNodesFromKlassIds = RelationshipRepository
          .getOtherSideEligibleNatureKRNodesFromKlassIds(removedTypes);
      fillExistingRelSidemap(eligibleNatureKRNodesFromKlassIds, notApplicableNatureSideIds);
      notApplicableNatureSideIds.removeAll(existingKnr);
    }
    
    String query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
        + "(select from klass where code in " + EntityUtil.quoteIt(existingKlassIds) + " )";
    Iterable<Vertex> krNodes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    fillExistingRelSidemap(krNodes, existingSideIds);
    
    if (!removedTaxonomies.isEmpty()) {
      query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
          + "(select from root_klass_taxonomy where code in "
          + EntityUtil.quoteIt(removedTaxonomies) + " )";
      krNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      fillExistingRelSidemap(krNodes, allRelationshipSidesToBeRemoved);
      existingTaxonomyIds.removeAll(removedTaxonomies);
    }
    if (!existingTaxonomyIds.isEmpty()) {
      query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
          + "(select from root_klass_taxonomy where code in "
          + EntityUtil.quoteIt(existingTaxonomyIds) + " )";
      krNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      fillExistingRelSidemap(krNodes, existingSideIds);
    }
    
    Set<String> newSideIds = new HashSet<>();
    if (!addedKlassIds.isEmpty()) {
      query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
          + "(select from klass where code in " + EntityUtil.quoteIt(addedKlassIds) + " )";
      krNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      fillExistingRelSidemap(krNodes, newSideIds);
    }
    
    if (!addedTaxonomyIds.isEmpty()) {
      query = "select expand(out('has_klass_property')[type = 'relationship'][isNature = false]) from "
          + "(select from root_klass_taxonomy where code in " + EntityUtil.quoteIt(addedTaxonomyIds)
          + " )";
      krNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      fillExistingRelSidemap(krNodes, newSideIds);
    }
    
    newSideIds.removeAll(existingSideIds);
    allRelationshipSidesToBeRemoved.removeAll(existingSideIds);
    return newSideIds;
  }
  
  private void fillExistingRelSidemap(Iterable<Vertex> krNodes, Set<String> existingSideIds)
  {
    for (Vertex side : krNodes) {
      String sideId = UtilClass.getCodeNew(side);
      existingSideIds.add(sideId);
    }
  }
}
