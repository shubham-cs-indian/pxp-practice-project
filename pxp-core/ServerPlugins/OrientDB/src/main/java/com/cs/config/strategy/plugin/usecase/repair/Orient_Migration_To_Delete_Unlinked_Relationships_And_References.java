package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_To_Delete_Unlinked_Relationships_And_References
    extends AbstractOrientMigration {
  
  public Orient_Migration_To_Delete_Unlinked_Relationships_And_References(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {
        "POST|Orient_Migration_To_Delete_Unlinked_Relationships_And_References/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Set<String> relationshipIdsToDelete = new HashSet<>();
    Set<String> referenceIdsToDelate = new HashSet<>();
    
    Iterable<Vertex> allRelationshipAndReferenceNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ROOT_RELATIONSHIP);
    filterIdsToDeleted(allRelationshipAndReferenceNodes, relationshipIdsToDelete,
        referenceIdsToDelate);
    
    RelationshipUtils.deleteRelationships(new ArrayList<>(relationshipIdsToDelete), new ArrayList<>());
   // ReferenceUtils.deleteReferences(new ArrayList<>(referenceIdsToDelate));
    
    relationshipIdsToDelete.addAll(referenceIdsToDelate);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IIdsListParameterModel.IDS, new ArrayList<>(relationshipIdsToDelete));
    
   // manageNotInheritedReferences();
    return responseMap;
  }
  
  protected void filterIdsToDeleted(Iterable<Vertex> allRelationshipAndReferenceNodes,
      Set<String> relationshipIdsToDelete, Set<String> referenceIdsToDelate)
  {
    for (Vertex relationshipAndReferenceNode : allRelationshipAndReferenceNodes) {
      String type = relationshipAndReferenceNode.getProperty(IRelationship.TYPE);
      Iterable<Vertex> kRNodes = relationshipAndReferenceNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex kRNode : kRNodes) {
        fillIdsToDeletedFromNodes(SystemLevelIds.RELATIONSHIP.equals(type) ? relationshipIdsToDelete
            : referenceIdsToDelate, relationshipAndReferenceNode, kRNode);
      }
    }
  }
  
  protected void fillIdsToDeletedFromNodes(Set<String> idsToDelete, Vertex relationshipNode,
      Vertex rKNode)
  {
    Iterator<Vertex> KlassNodes = rKNode
        .getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
        .iterator();
    if (!KlassNodes.hasNext()) {
      idsToDelete.add(UtilClass.getCodeNew(relationshipNode));
    }
  }
  
  /* @SuppressWarnings("unchecked")
  private void manageNotInheritedReferences()
  {
    // String query4 = "select from Has_Klass_Reference where isInherited =
    // false AND inV().isNature = false";
    
    // query to get all klassReferences that are for normal reference
    String queryToGetKlassesLinkedDirectlyWithReference = "select from "
        + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where outE('"
        + RelationshipLabelConstants.HAS_KLASS_REFERENCE + "')."
        + CommonConstants.IS_INHERITED_PROPERTY + " contains false AND out('"
        + RelationshipLabelConstants.HAS_KLASS_REFERENCE + "')." + ISectionReference.IS_NATURE
        + " contains false";
    
    Iterable<Vertex> listOfVertices = UtilClass.getGraph()
        .command(new OCommandSQL(queryToGetKlassesLinkedDirectlyWithReference))
        .execute();
    
    for (Vertex classVertex : listOfVertices) {
      // query to get all children
      String querytoGetKlassesQualifiyingForReferenceInheritance = "select from (TRAVERSE in('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + classVertex.getId()
          + " STRATEGY BREADTH_FIRST) where ( " + IKlass.IS_NATURE + " = true OR "
          + IKlass.IS_STANDARD + "= true)";
      
      Iterable<Vertex> childVerticesIterable = UtilClass.getGraph()
          .command(new OCommandSQL(querytoGetKlassesQualifiyingForReferenceInheritance))
          .execute();
      
      List<Vertex> childVertices = IteratorUtils.toList(childVerticesIterable.iterator());
      Iterable<Vertex> klassReferences = classVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_REFERENCE);
      
      for (Vertex klassReference : klassReferences) {
        Boolean isNature = klassReference.getProperty(ISectionReference.IS_NATURE);
        if (isNature) {
          continue;
        }
        
        List<Vertex> verticesWithLinkToReference = IteratorUtils.toList(
            klassReference.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_REFERENCE)
                .iterator());
        
        if (!verticesWithLinkToReference.containsAll(childVertices)) {
          List<Vertex> childVerticesToUpdate = childVertices.stream()
              .filter(x -> !verticesWithLinkToReference.contains(x))
              .collect(Collectors.toList());
          
          for (Vertex collect : childVerticesToUpdate) {
            Edge klassPropertyEdge = collect.addEdge(RelationshipLabelConstants.HAS_KLASS_REFERENCE,
                klassReference);
            klassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
            klassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
                new ArrayList<String>());
          }
        }
      }
    }
  }*/
}
