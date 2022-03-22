package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_Relationship extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Relationship(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Relationship/*" };
  }
  
  @SuppressWarnings("unlikely-arg-type")
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    
    UtilClass.getDatabase()
        .commit();
    
    OrientEdgeType edge = UtilClass
        .getOrCreateEdgeType(RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    OrientEdgeType superClassEdge = UtilClass
        .getOrCreateEdgeType(RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    edge.addSuperClass(superClassEdge);
    
    Iterable<Vertex> rootRelationshipVertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ROOT_RELATIONSHIP);
    
    for (Vertex rootRelationshipVertex : rootRelationshipVertices) {
      
      Map<String, Object> side1 = rootRelationshipVertex.getProperty(IRelationship.SIDE1);
      side1.put(IRelationshipSide.CODE, side1.get(IRelationshipSide.ID));
      Map<String, Object> side2 = rootRelationshipVertex.getProperty(IRelationship.SIDE2);
      side2.put(IRelationshipSide.CODE, side2.get(IRelationshipSide.ID));
      
      if (rootRelationshipVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
          .equals(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP)) {
        
        Iterable<Vertex> klassRelationshipVertices = rootRelationshipVertex
            .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
        
        for (Vertex klassRelationshipVertex : klassRelationshipVertices) {
          setElementId(klassRelationshipVertex, rootRelationshipVertex);
          
          klassRelationshipVertex.setProperty(IRelationship.IS_NATURE, false);
        }
        rootRelationshipVertex.setProperty(IRelationship.IS_NATURE, false);
      }
      else if (rootRelationshipVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
          .equals(VertexLabelConstants.NATURE_RELATIONSHIP)) {
        
        Iterable<Vertex> klassRelationshipVertices = rootRelationshipVertex
            .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
        
        for (Vertex klassRelationshipVertex : klassRelationshipVertices) {
          klassRelationshipVertex.setProperty(IRelationship.IS_NATURE, false);
          setElementId(klassRelationshipVertex, rootRelationshipVertex);
        }
        
        Iterator<Vertex> klassNatureRelationshipVertices = rootRelationshipVertex
            .getVertices(Direction.IN, "Has_Relationship")
            .iterator();
        
        if (klassNatureRelationshipVertices.hasNext()) {
          
          Vertex klassNatureRelationshipVertex = klassNatureRelationshipVertices.next();
          
          Iterator<Vertex> mainKlassVertices = klassNatureRelationshipVertex
              .getVertices(Direction.IN, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
              .iterator();
          
          Map<String, Object> mapFromNode = UtilClass.getMapFromNode(klassNatureRelationshipVertex);
          OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
              VertexLabelConstants.KLASS_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
          
          Vertex newKrVertex = UtilClass.createNode(mapFromNode, vertexType, new ArrayList<>());
          
          UtilClass.getGraph()
              .addEdge(null, newKrVertex, rootRelationshipVertex,
                  RelationshipLabelConstants.HAS_PROPERTY);
          
          if (mainKlassVertices.hasNext()) {
            UtilClass.getGraph()
                .addEdge(null, mainKlassVertices.next(), newKrVertex,
                    RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
          }
          
          newKrVertex.setProperty(IRelationship.IS_NATURE, true);
          
          linkRelationshipContextToNewKrNode(klassNatureRelationshipVertex, newKrVertex);
          
          linkTransferAttributeToNewKrNode(klassNatureRelationshipVertex, newKrVertex);
          
          linkTransferTagToNewKRNode(klassNatureRelationshipVertex, newKrVertex);
          
          setElementId(newKrVertex, rootRelationshipVertex);
          
          UtilClass.getGraph()
              .removeVertex(klassNatureRelationshipVertex);
          rootRelationshipVertex.setProperty(IRelationship.IS_NATURE, true);
        }
      }
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  private void linkRelationshipContextToNewKrNode(Vertex klassNatureRelationshipVertex,
      Vertex newKrVertex)
  {
    Iterator<Vertex> relationshipContext = klassNatureRelationshipVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (relationshipContext.hasNext()) {
      newKrVertex.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF,
          relationshipContext.next());
    }
  }
  
  private void linkTransferAttributeToNewKrNode(Vertex klassNatureRelationshipVertex,
      Vertex newKrVertex)
  {
    Iterator<Vertex> transferAttributes = klassNatureRelationshipVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE)
        .iterator();
    while (transferAttributes.hasNext()) {
      Vertex attribute = transferAttributes.next();
      Iterator<Edge> edges = attribute
          .getEdges(Direction.IN, RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE)
          .iterator();
      if (edges.hasNext()) {
        String couplingType = edges.next()
            .getProperty(ISectionElement.COUPLING_TYPE);
        Edge addEdge = newKrVertex.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE,
            attribute);
        addEdge.setProperty(ISectionElement.COUPLING_TYPE, couplingType);
      }
    }
  }
  
  private void linkTransferTagToNewKRNode(Vertex klassNatureRelationshipVertex, Vertex newKrVertex)
  {
    Iterator<Vertex> transferTag = klassNatureRelationshipVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
        .iterator();
    while (transferTag.hasNext()) {
      Vertex tag = transferTag.next();
      Iterator<Edge> edges = tag
          .getEdges(Direction.IN, RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
          .iterator();
      if (edges.hasNext()) {
        String couplingType = edges.next()
            .getProperty(ISectionElement.COUPLING_TYPE);
        Edge addEdge = newKrVertex.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_TAG, tag);
        addEdge.setProperty(ISectionElement.COUPLING_TYPE, couplingType);
      }
    }
  }
  
  public void setElementId(Vertex klassRelationshipVertex, Vertex rootRelationshipVertex)
  {
    if (klassRelationshipVertex.getProperty(CommonConstants.SIDE_PROPERTY)
        .equals(IRelationship.SIDE1)) {
      Map<String, Object> side1 = rootRelationshipVertex.getProperty(IRelationship.SIDE1);
      side1.put(IRelationshipSide.ELEMENT_ID,
          klassRelationshipVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    else {
      Map<String, Object> side2 = rootRelationshipVertex.getProperty(IRelationship.SIDE2);
      side2.put(IRelationshipSide.ELEMENT_ID,
          klassRelationshipVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
}
// { "id": "7",
// "pluginName":"Orient_Migration_Script_Relationship",
// "createdOnAsString":"23/08/2018",
// "description":"This migration script removes KNR node, add new KR node with
// edges KLASS_NATURE_RELATIONSHIP_OF with main node and HAS_PROPERTY with
// nature relationship node."
// }
