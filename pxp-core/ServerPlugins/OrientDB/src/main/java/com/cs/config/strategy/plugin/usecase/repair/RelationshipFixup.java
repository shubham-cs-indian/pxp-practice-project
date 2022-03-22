package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class RelationshipFixup {
  
  public static void inheritKlassRelationship(Vertex parentKlass, Vertex klassRelationshipVertex)
  {
    Iterable<Edge> klassRelationshipLinks = klassRelationshipVertex.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    boolean hasInheritingRelationships = false;
    for (Edge klassRelationshipLink : klassRelationshipLinks) {
      Boolean isInheriting = klassRelationshipLink.getProperty("isInherited");
      if (isInheriting) {
        System.out.println("Relationship is already being inherited");
        hasInheritingRelationships = true;
        break;
      }
    }
    if (!hasInheritingRelationships) {
      System.out.println("Found Non-Inheriting Relationships");
      Vertex relationshipVertex = null;
      Iterable<Vertex> relationshipVertices = klassRelationshipVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex vertex2 : relationshipVertices) {
        relationshipVertex = vertex2;
      }
      if (relationshipVertex != null) {
        System.out.println("Fixing Relationships");
        RelationshipUtils.inheritKlassRelationshipNodesInChildKlasses(parentKlass,
            klassRelationshipVertex, false);
      }
    }
  }
}
