package com.cs.config.strategy.plugin.usecase.template.util;

public class DeleteTemplateUtils {
  /*
  public static void deleteTemplateNode(Vertex templateNode)
  {
    Iterator<Vertex> headerIterator = templateNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_HEADER)
        .iterator();
    if (headerIterator.hasNext()) {
      Vertex header = headerIterator.next();
      header.remove();
    }
  
    Iterable<Vertex> tabs = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TEMPLATE_TAB);
    for (Vertex tab : tabs) {
      tab.remove();
    }
  
    deletePropertyCollectionSequence(templateNode);
    deleteRelationshipSequence(templateNode);
    deletePermissionNodes(templateNode);
    templateNode.remove();
  }
  
  /**
   * @author Lokesh
   * @param templateNode
   */
  /*
  private static void deletePermissionNodes(Vertex templateNode)
  {
    Iterable<Vertex> permissionIterable = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  
    permissionIterable = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  
    permissionIterable = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  
    permissionIterable = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TAB_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  
    permissionIterable = templateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_HEADER_PERMISSION);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  /**
   * @author Arshad
   * @param templateNode
   * This method deletes the PCsequence node associated with template
   */
  /*
  public static void deletePropertyCollectionSequence(Vertex templateNode)
  {
    Iterator<Edge> propertyCollectionSequenceIterator = templateNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE)
        .iterator();
    if (propertyCollectionSequenceIterator.hasNext()) {
      Edge properyCollectionSequenceEdge = propertyCollectionSequenceIterator.next();
      Boolean isInherited = properyCollectionSequenceEdge
          .getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (!isInherited) {
        Vertex properyCollectionSequenceNode = properyCollectionSequenceEdge
            .getVertex(Direction.IN);
        properyCollectionSequenceNode.remove();
      }
    }
  }
  
  /**
   * @author Arshad
   * @param templateNode
   * This method deletes the relationshipsequence node associated with template
   */
  /*
  public static void deleteRelationshipSequence(Vertex templateNode)
  {
    Iterator<Edge> relationshipSequenceIterator = templateNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE)
        .iterator();
    if (relationshipSequenceIterator.hasNext()) {
      Edge relationshipSequenceEdge = relationshipSequenceIterator.next();
      Boolean isInherited = relationshipSequenceEdge
          .getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (!isInherited) {
        Vertex relationshipSequenceNode = relationshipSequenceEdge.getVertex(Direction.IN);
        relationshipSequenceNode.remove();
      }
    }
  }*/
}
