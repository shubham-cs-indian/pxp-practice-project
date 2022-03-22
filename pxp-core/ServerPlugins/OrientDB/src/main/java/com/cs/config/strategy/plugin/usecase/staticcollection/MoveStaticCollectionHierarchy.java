package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.config.interactor.exception.staticcollection.IllegalCollectionHierarchyMoveException;
import com.cs.core.config.interactor.model.collections.IMoveCollectionNodeModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.Map;

public class MoveStaticCollectionHierarchy extends AbstractOrientPlugin {
  
  public MoveStaticCollectionHierarchy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MoveStaticCollectionHierarchy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    String collectionId = (String) requestMap.get(IMoveCollectionNodeModel.COLLECTION_ID);
    String parentId = (String) requestMap.get(IMoveCollectionNodeModel.PARENT_ID);
    Vertex collectionNode = null;
    try {
      collectionNode = UtilClass.getVertexById(collectionId, VertexLabelConstants.COLLECTION);
      Iterator<Vertex> sectionNodesIterator = collectionNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      if (sectionNodesIterator.hasNext()) {
        throw new IllegalCollectionHierarchyMoveException();
      }
    }
    catch (NotFoundException e) {
      throw new CollectionNodeNotFoundException(e);
    }
    Vertex parentNode = null;
    try {
      parentNode = UtilClass.getVertexById(parentId, VertexLabelConstants.COLLECTION);
      String query = "select from(traverse in('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from "
          + collectionNode.getId() + ")";
      Iterable<Vertex> childrenNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex childNode : childrenNodes) {
        if (childNode.getId()
            .equals(parentNode.getId())) {
          throw new IllegalCollectionHierarchyMoveException();
        }
      }
      Iterator<Vertex> sectionNodesIterator = collectionNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      if (sectionNodesIterator.hasNext()) {
        throw new IllegalCollectionHierarchyMoveException();
      }
    }
    catch (NotFoundException e) {
      throw new CollectionNodeNotFoundException(e);
    }
    
    Iterator<Vertex> currentParentNodeIterator = collectionNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    if (currentParentNodeIterator.hasNext()) {
      if (currentParentNodeIterator.next()
          .getId()
          .equals(parentNode.getId())) {
        throw new IllegalCollectionHierarchyMoveException();
      }
    }
    Iterator<Edge> edge = collectionNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    if (edge.hasNext()) {
      Edge chilOfEdge = edge.next();
      chilOfEdge.remove();
    }
    // Moving collection in another hierarchy,by creating link with new parent
    collectionNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
    UtilClass.getGraph()
        .commit();
    return requestMap;
  }
}
