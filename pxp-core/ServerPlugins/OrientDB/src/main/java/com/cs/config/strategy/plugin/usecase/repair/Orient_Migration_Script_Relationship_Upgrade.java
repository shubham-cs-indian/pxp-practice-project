package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.relationship.IRelationshipInfoModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** @author Ajit */
public class Orient_Migration_Script_Relationship_Upgrade extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Relationship_Upgrade(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Relationship_Upgrade/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> selfRelationshipIds = new ArrayList<>();
    List<String> selfNatureRelationshipIds = new ArrayList<>();
    Map<String, Object> relationshipInformationMap = new HashMap<>();
    String hasPropertyEdge = RelationshipLabelConstants.HAS_PROPERTY;
    
    // Has Relationship has been changed .Neecd to handle usecase for new knr.
    // String hasRelationshipEdge = RelationshipLabelConstants.HAS_RELATIONSHIP;
    
    // Repair all self relationship nodes and fill relationship info map
    Iterable<Vertex> relationshipNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    for (Vertex relationshipNode : relationshipNodes) {
      
      Map<String, Object> side1 = relationshipNode.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2 = relationshipNode.getProperty(IRelationship.SIDE2);
      String klass1Id = (String) side1.get(IRelationshipSide.KLASS_ID);
      String klass2Id = (String) side2.get(IRelationshipSide.KLASS_ID);
      
      /*Check if this is self relationship*/
      if (klass1Id.equals(klass2Id)) {
        repairSelfRelationships(relationshipNode, klass1Id, relationshipInformationMap,
            selfRelationshipIds, hasPropertyEdge);
      }
      
      // Fill relationhips info => relationshipId Vs krIds Vs klassIds map
      fillRelationshipsInfo(relationshipInformationMap, relationshipNode);
    }
    
    // Repair all self relationship nodes and fill relationship info map
    Iterable<Vertex> natureRelationshipNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.NATURE_RELATIONSHIP);
    for (Vertex relationshipNode : natureRelationshipNodes) {
      
      Map<String, Object> side1 = relationshipNode.getProperty(IRelationship.SIDE1);
      Map<String, Object> side2 = relationshipNode.getProperty(IRelationship.SIDE2);
      String klass1Id = (String) side1.get(IRelationshipSide.KLASS_ID);
      String klass2Id = (String) side2.get(IRelationshipSide.KLASS_ID);
      
      /*Check if this is self relationship*/
      if (klass1Id.equals(klass2Id)) {
        // Has Relationship has been changed .Neecd to handle usecase for new
        // knr.
        // repairSelfRelationships(relationshipNode, klass1Id,
        // relationshipInformationMap,
        // selfNatureRelationshipIds, hasRelationshipEdge);
      }
      
      // Fill relationhips info => relationshipId Vs krIds Vs klassIds map
      fillNatureRelationshipsInfo(relationshipInformationMap, relationshipNode);
    }
    
    System.out.println("----------------------------------------------------------------");
    System.out.println("Self relationship Ids repaired " + selfRelationshipIds);
    System.out.println("Self Nature relationship Ids repaired " + selfNatureRelationshipIds);
    System.out.println(relationshipInformationMap);
    System.out.println("----------------------------------------------------------------");
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IRelationshipInfoModel.RELATIONSHIP_INFO, relationshipInformationMap);
    return returnMap;
  }
  
  private void fillRelationshipsInfo(Map<String, Object> relationshipInformationMap,
      Vertex relationshipNode)
  {
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    Map<String, Object> sidesMap = new HashMap<>();
    Iterator<Vertex> krNodesIterator = relationshipNode
        .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    while (krNodesIterator.hasNext()) {
      Vertex krNode = krNodesIterator.next();
      String krId = UtilClass.getCodeNew(krNode);
      Iterator<Vertex> klassNodesiterator = krNode
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
          .iterator();
      List<String> klassIds = new ArrayList<>();
      while (klassNodesiterator.hasNext()) {
        Vertex klassNode = klassNodesiterator.next();
        String klassId = UtilClass.getCodeNew(klassNode);
        klassIds.add(klassId);
      }
      sidesMap.put(krId, klassIds);
    }
    relationshipInformationMap.put(relationshipId, sidesMap);
  }
  
  private void fillNatureRelationshipsInfo(Map<String, Object> relationshipInformationMap,
      Vertex relationshipNode)
  {
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    Map<String, Object> sidesMap = new HashMap<>();
    
    Iterator<Vertex> krNodesIterator = relationshipNode
        .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    while (krNodesIterator.hasNext()) {
      Vertex krNode = krNodesIterator.next();
      String krId = UtilClass.getCodeNew(krNode);
      Iterator<Vertex> klassNodesiterator = krNode
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
          .iterator();
      List<String> klassIds = new ArrayList<>();
      while (klassNodesiterator.hasNext()) {
        Vertex klassNode = klassNodesiterator.next();
        String klassId = UtilClass.getCodeNew(klassNode);
        klassIds.add(klassId);
      }
      sidesMap.put(krId, klassIds);
    }
    
    // Deleted HAS_RELATIONSHIP relationlabelconstants. Need To handle this for
    // new KNR
    // Iterator<Vertex> knrNodesIterator =
    // relationshipNode.getVertices(Direction.IN,
    // RelationshipLabelConstants.HAS_RELATIONSHIP).iterator();
    // while(knrNodesIterator.hasNext()){
    // Vertex krNode = knrNodesIterator.next();
    // String krId = UtilClass.getCode(krNode);
    // Iterator<Vertex> klassNodesiterator = krNode.getVertices(Direction.IN,
    // RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF).iterator();
    // List<String> klassIds = new ArrayList<>();
    // while(klassNodesiterator.hasNext()){
    // Vertex klassNode = klassNodesiterator.next();
    // String klassId = UtilClass.getCode(klassNode);
    // klassIds.add(klassId);
    // }
    // sidesMap.put(krId, klassIds);
    // }
    relationshipInformationMap.put(relationshipId, sidesMap);
  }
  
  @SuppressWarnings({ "deprecation", "unchecked" })
  private void repairSelfRelationships(Vertex relationshipNode, String klass1Id,
      Map<String, Object> relationshipInformationMap, List<String> selfRelationshipIds,
      String hasPropertyEdge) throws Exception
  {
    Vertex klassNode = UtilClass.getVertexById(klass1Id,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Iterator<Vertex> krNodesIterator = relationshipNode.getVertices(Direction.IN, hasPropertyEdge)
        .iterator();
    Vertex kr1Node = krNodesIterator.next();
    if (krNodesIterator.hasNext()) {
      // This means that this self relationship already has 2 KRs. No need to
      // repair
      return;
    }
    
    OrientVertexType krVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.KLASS_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
    
    Map<String, Object> kr1Map = UtilClass.getMapFromVertex(new ArrayList<>(), kr1Node);
    String kr1Id = UtilClass.getCodeNew(kr1Node);
    String kr2Id = UtilClass.getUniqueSequenceId(krVertexType);
    
    Map<String, Object> relationshipSide1 = kr1Node.getProperty(CommonConstants.RELATIONSHIP_SIDE);
    relationshipSide1.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID, kr2Id);
    kr1Node.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide1);
    
    Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(new ArrayList<>(),
        relationshipNode);
    Map<String, Object> relationshipNodeSide1Map = (Map<String, Object>) relationshipMap
        .get(CommonConstants.RELATIONSHIP_SIDE_1);
    String side1IdFromRelationshipNode = (String) relationshipNodeSide1Map
        .get(CommonConstants.ID_PROPERTY);
    Map<String, Object> relationshipSide2 = new HashMap<>(relationshipSide1);
    relationshipSide2.put(IKlassRelationshipSide.ID, side1IdFromRelationshipNode);
    relationshipSide2.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID, kr1Id);
    
    Map<String, Object> kr2Map = new HashMap<>(kr1Map);
    kr2Map.put(CommonConstants.RELATIONSHIP_SIDE, relationshipSide2);
    kr2Map.put(CommonConstants.CODE_PROPERTY, kr2Id);
    kr2Map.put(CommonConstants.CODE_PROPERTY, kr2Id);
    kr2Map.put(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_2);
    kr2Map.put(CommonConstants.VERSION_ID, 0l);
    
    Vertex kr2Node = UtilClass.createNode(kr2Map, krVertexType);
    
    // connect to relationship node
    kr2Node.addEdge(RelationshipLabelConstants.HAS_PROPERTY, relationshipNode);
    
    // connect to klass and its children
    String rid = (String) klassNode.getId()
        .toString();
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + "  strategy BREADTH_FIRST)"))
        .execute();
    for (Vertex childKlassNode : iterable) {
      String childklassId = UtilClass.getCodeNew(childKlassNode);
      if (childklassId.equals(klass1Id)) {
        Edge hasKlassProperty = childKlassNode
            .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, kr2Node);
        hasKlassProperty.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
      }
      else {
        
        // For child klasses
        Edge hasKlassProperty = childKlassNode
            .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, kr2Node);
        hasKlassProperty.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      }
    }
    
    // connect to context nodes
    Iterator<Vertex> contextIterator = kr1Node
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF)
        .iterator();
    if (contextIterator.hasNext()) {
      Vertex contextNode = contextIterator.next();
      contextNode.addEdge(RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF, kr2Node);
    }
    
    // connect to extension nodes
    Iterator<Vertex> extensionIterator = kr1Node
        .getVertices(Direction.OUT, "Has_Relationship_Extension")
        .iterator();
    if (extensionIterator.hasNext()) {
      Vertex extensionNode = extensionIterator.next();
      Vertex extensionKlassNode = extensionNode
          .getVertices(Direction.OUT, "Has_Relationship_Extension")
          .iterator()
          .next();
      
      OrientVertexType extensionVertexType = UtilClass.getOrCreateVertexType(
          "Relationship_Extension", CommonConstants.CODE_PROPERTY);
      
      Map<String, Object> extensionMap = new HashMap<>();
      String extensionId = UtilClass.getUniqueSequenceId(extensionVertexType);
      extensionMap.put(CommonConstants.ID_PROPERTY, extensionId);
      extensionMap.put(CommonConstants.CODE_PROPERTY, extensionId);
      
      Vertex newExtensionNode = UtilClass.createNode(extensionMap, extensionVertexType);
      kr2Node.addEdge("Has_Relationship_Extension", newExtensionNode);
      newExtensionNode.addEdge("Relationhip_Extension_Of", extensionKlassNode);
    }
    
    selfRelationshipIds.add(UtilClass.getCodeNew(relationshipNode));
  }
}
