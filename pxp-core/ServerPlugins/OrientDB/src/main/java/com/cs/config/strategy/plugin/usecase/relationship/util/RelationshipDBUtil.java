package com.cs.config.strategy.plugin.usecase.relationship.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.List;
import javax.management.relation.RelationNotFoundException;

public class RelationshipDBUtil {
  
  /**
   * @author Aayush
   * @param klassRelationship
   * @return
   * @throws Exception
   */
  public static Vertex getContextFromKlassRelationshipVertex(Vertex klassRelationship)
      throws Exception
  {
    
    Iterator<Vertex> iterator = klassRelationship
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF)
        .iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    Vertex context = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return context;
  }
  
  /**
   * @author Aayush
   * @param klassRelationship
   * @return
   * @throws Exception
   */
  public static Vertex getLinkedVariantContextFromKlassRelationshipVertex(Vertex klassRelationship)
      throws Exception
  {
    
    Iterator<Vertex> iterator = klassRelationship
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (!iterator.hasNext()) {
      return null;
    }
    
    Vertex context = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return context;
  }
  
  /**
   * get relationship vertex from klass relationship vertex
   *
   * @author Aayush
   * @param klassRelationshipVertex
   * @return
   * @throws RelationNotFoundException
   * @throws MultipleLinkFoundException
   */
  public static Vertex getRelationshipVertexFromKlassRelationship(Vertex klassRelationshipVertex)
      throws RelationNotFoundException, MultipleLinkFoundException
  {
    Iterator<Vertex> relationshipVertices = klassRelationshipVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    
    if (!relationshipVertices.hasNext()) {
      throw new RelationNotFoundException();
    }
    
    Vertex relationshipVertex = relationshipVertices.next();
    if (relationshipVertices.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    
    return relationshipVertex;
  }
  
  public static Iterable<Vertex> getNonInheritedRelationshipVerticesFromTypes(List<String> typeIds)
  {
    String query = "select expand(outE('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')[ "
        + CommonConstants.IS_INHERITED_PROPERTY + " = false OR "
        + CommonConstants.IS_INHERITED_PROPERTY + " is null].inV()[ type = 'relationship' ].out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')) from " + typeIds;
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return iterable;
  }
  
  public static Iterable<Vertex> getNatureRelationshipVerticesFromTypes(List<String> typeIds)
  {
    String query = "select expand(outE('" + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF
        + "').inV()[ type = 'relationship' ].out('" + RelationshipLabelConstants.HAS_PROPERTY
        + "')) from " + typeIds;
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return iterable;
  }
}
