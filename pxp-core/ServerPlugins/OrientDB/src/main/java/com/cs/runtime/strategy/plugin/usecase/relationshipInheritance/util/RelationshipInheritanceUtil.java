package com.cs.runtime.strategy.plugin.usecase.relationshipInheritance.util;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class RelationshipInheritanceUtil {
  
  /**
   * Description : Merges coupling type of relationships to be inherited.
   *
   * @author Rohit.Raina
   * @param mapToReturn
   * @param klassIds
   * @param helperModel
   */
  public static void fillRelationshipInheritanceInfo(Map<String, Object> mapToReturn,
      List<String> klassIds)
  {
    Map<String, Object> referenceElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referenceRelationships = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    
    fillRelationshipInheritanceInfo(klassIds, referenceElements, referenceRelationships);
  }
  
  private static void fillRelationshipInheritanceInfo(List<String> klassIds,
      Map<String, Object> referenceElements, Map<String, Object> referenceRelationships)
  {
    Iterable<Vertex> natureKRs = RelationshipRepository
        .getOtherSideEligibleNatureKRNodesFromKlassIds(klassIds);
    
    for (Vertex natureKR : natureKRs) {
      Iterable<Edge> inheritanceRelationshipEdges = natureKR.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
      for (Edge inheritanceRelationshipEdge : inheritanceRelationshipEdges) {
        Vertex relationshipNode = inheritanceRelationshipEdge.getVertex(Direction.IN);
        Vertex natureRelationshipNode = inheritanceRelationshipEdge.getVertex(Direction.OUT)
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator()
            .next();
        String relationshipId = UtilClass.getCodeNew(relationshipNode);
        String natureRelationshipId = UtilClass.getCodeNew(natureRelationshipNode);
        
        Map<String, Object> referenceRelationship = (Map<String, Object>) referenceRelationships
            .get(relationshipId);
        
        /**
         * May be some relationships are configured in config, but not
         * applicable at runtime, so skip them.
         */
        if (referenceRelationship == null) {
          continue;
        }
        
        String side1Id = (String) ((Map<String, Object>) referenceRelationship
            .get(IReferencedRelationshipModel.SIDE1)).get(CommonConstants.SIDE_ID);
        String side2Id = (String) ((Map<String, Object>) referenceRelationship
            .get(IReferencedRelationshipModel.SIDE2)).get(CommonConstants.SIDE_ID);
        
        String couplingType = inheritanceRelationshipEdge
            .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        if (referenceElements.containsKey(side1Id)) {
          fillRelationshipConflictingInfo(referenceElements, natureRelationshipId, side1Id,
              couplingType);
        }
        if (referenceElements.containsKey(side2Id)) {
          fillRelationshipConflictingInfo(referenceElements, natureRelationshipId, side2Id,
              couplingType);
        }
      }
    }
  }
  
  private static void fillRelationshipConflictingInfo(Map<String, Object> referenceElements,
      String natureRelationshipId, String side1Id, String couplingType)
  {
    Map<String, Object> referenceElement = (Map<String, Object>) referenceElements.get(side1Id);
    
    Map<String, Object> conflictingSource = new HashMap<>();
    conflictingSource.put(IElementConflictingValuesModel.ID, natureRelationshipId);
    conflictingSource.put(IElementConflictingValuesModel.TYPE,
        referenceElement.get(IReferencedSectionElementModel.TYPE));
    conflictingSource.put(IElementConflictingValuesModel.SOURCE_TYPE, "relationshipInheritance");
    
    String oldCouplingType = (String) referenceElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    List<Map<String, Object>> conflictingSources = (List<Map<String, Object>>) referenceElement
        .get(IReferencedSectionElementModel.CONFLICTING_SOURCES);
    if (conflictingSources == null) {
      conflictingSources = new ArrayList<>();
      referenceElement.put(IReferencedSectionElementModel.CONFLICTING_SOURCES, conflictingSources);
    }
    
    /*  conflictingSources.clear();
    referenceElement.put(IReferencedSectionElementModel.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
    conflictingSources.add(conflictingSource);*/
      Integer couplingNumber = EntityUtil.compareCoupling(oldCouplingType, couplingType);
    if (couplingNumber > 0) {
      conflictingSources.clear();
      conflictingSources.add(conflictingSource);
      referenceElement.put(IReferencedSectionElementModel.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
    }
    if (couplingNumber == 0) {
      conflictingSources.add(conflictingSource);
    }
  }
  
  public static void fillCouplingTypeWithRespectToRelationship(
      Map<String, Object> referencedNatureRelationshipsMap, Vertex natureRelationshipNode,
      Set<String> propagableRelationshipIds)
  {
    String relationshipInheritanceQuery = "select expand(in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')[side = 'side1'].outE('"
        + RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP + "'))from "
        + natureRelationshipNode.getId();
    Iterable<Edge> relationshipInheritanceEdges = UtilClass.getGraph()
        .command(new OCommandSQL(relationshipInheritanceQuery))
        .execute();
    
    Map<String, Object> referencedNatureRelationshipMap = (Map<String, Object>) referencedNatureRelationshipsMap
        .get(UtilClass.getCodeNew(natureRelationshipNode));
    
    Map<String, Object> relationshipIdVSCouplingInfo = new HashMap<>();
    referencedNatureRelationshipMap.put(
        IReferencedNatureRelationshipInheritanceModel.PROPAGABLE_RELATIONSHIP_IDS_COUPLING_TYPE,
        relationshipIdVSCouplingInfo);
    fillCouplingType(relationshipInheritanceEdges, relationshipIdVSCouplingInfo);
    propagableRelationshipIds.addAll(relationshipIdVSCouplingInfo.keySet());
  }
  
  private static void fillCouplingType(Iterable<Edge> relationshipInheritanceEdges,
      Map<String, Object> relationshipIdVSCouplingInfo)
  {
    for (Edge relationshipInheritanceEdge : relationshipInheritanceEdges) {
      Vertex relationshipNode = relationshipInheritanceEdge.getVertex(Direction.IN);
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      String couplingType = relationshipInheritanceEdge
          .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      Map<String, Object> couplingInfo = new HashMap<>();
      couplingInfo.put(IIdAndCouplingTypeModel.ID, relationshipId);
      couplingInfo.put(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
      relationshipIdVSCouplingInfo.put(relationshipId, couplingInfo);
    }
  }
  
  public static void getNatureRelationshipMapForRelationshipInheritance(
      Map<String, Object> referencedNatureRelationshipsMap, Vertex natureRelationshipNode)
      throws Exception
  {
    Set<String> klass2SideIds = new HashSet<String>();
    Map<String, Object> natureRelationshipMap = RelationshipUtils
        .getNatureRelationshipMap(natureRelationshipNode);
    Iterable<Vertex> side2KlassNodes = RelationshipRepository
        .getSide2KlassNodesOfNatureRelationship(natureRelationshipNode);
    
    for (Vertex side2KlassNode : side2KlassNodes) {
      klass2SideIds.add(UtilClass.getCodeNew(side2KlassNode));
    }
    Map<String, Object> side1 = (Map<String, Object>) natureRelationshipMap
        .get(IReferencedNatureRelationshipInheritanceModel.SIDE1);
    String klassId = (String) side1.get(IRelationshipSide.KLASS_ID);
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE1_KLASS_IDS,
        Arrays.asList(klassId));
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE2_KLASS_IDS,
        klass2SideIds);
    
    referencedNatureRelationshipsMap.put(
        (String) natureRelationshipMap.get(IReferencedNatureRelationshipInheritanceModel.ID),
        natureRelationshipMap);
  }
  
  public static void getRelationshipMapForRelationshipInheritance(
      Map<String, Object> referencedRelationshipsMap, String relationshipId) throws Exception
  {
    Vertex relationshipVertex = UtilClass.getVertexByIndexedId(relationshipId,
        VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    Map<String, Object> relationshipEntityMap = RelationshipUtils
        .getRelationshipEntityMap(relationshipVertex);
    
    Iterable<Vertex> KRVertices = relationshipVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex KRVertex : KRVertices) {
      List<String> sideKlassIds = new ArrayList<>();
      List<String> sideTaxonomyIds = new ArrayList<>();
      String side = KRVertex.getProperty(CommonConstants.SIDE_PROPERTY);
      Iterable<Vertex> klassVertices = KRVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassVertex : klassVertices) {
        String entityId = klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
        if (klassVertex.getProperty("@class")
            .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
          sideTaxonomyIds.add(entityId);
        }
        else {
          sideKlassIds.add(entityId);
        }
      }
      if (side.equals(CommonConstants.RELATIONSHIP_SIDE_1)) {
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE1_KLASS_IDS,
            sideKlassIds);
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE1_TAXONOMY_IDS,
            sideTaxonomyIds);
      }
      else {
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE2_KLASS_IDS,
            sideKlassIds);
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE2_TAXONOMY_IDS,
            sideTaxonomyIds);
      }
    }
    referencedRelationshipsMap.put(relationshipId, relationshipEntityMap);
  }
  
  public static void fillReferenceRelationshipInfoForRelationshipInheritance(
      List<String> relationshipIds, Map<String, Object> referencedRelationshipsMap) throws Exception
  {
    for (String relationshipId : relationshipIds) {
      getRelationshipMapForRelationshipInheritance(referencedRelationshipsMap, relationshipId);
    }
  }
  
  public static List<String> fillReferencedNRForRelationshipInheritance(
      Map<String, Object> referencedNatureRelationshipsMap,
      Iterable<Vertex> natureRelationshipNodes) throws Exception
  {
    Set<String> propagableRelationshipIds = new HashSet<String>();
    for (Vertex natureRelationshipNode : natureRelationshipNodes) {
      getNatureRelationshipMapForRelationshipInheritance(referencedNatureRelationshipsMap,
          natureRelationshipNode);
      fillCouplingTypeWithRespectToRelationship(referencedNatureRelationshipsMap,
          natureRelationshipNode, propagableRelationshipIds);
    }
    return new ArrayList<>(propagableRelationshipIds);
  }
}
