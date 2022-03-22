/*
 * (SetStandardAttributesDisabled) : This migration script dated 30-08-2016 is
 * to update flat field attributes to isDisabled true.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import java.util.HashMap;
import java.util.Map;

public class Orient_Inherit_Non_Inheriting_Relationships extends AbstractOrientPlugin {
  
  public Orient_Inherit_Non_Inheriting_Relationships(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_KLASS);
    for (Vertex klassVertex : vertices) {
      Iterable<Edge> hasKlassPropertyEdges = klassVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Edge hasKlassProperty : hasKlassPropertyEdges) {
        Boolean isInherited = hasKlassProperty.getProperty("isInherited");
        Vertex klassProperty = hasKlassProperty.getVertex(Direction.OUT);
        if (((OrientVertex) klassProperty).getLabel()
            .equals(VertexLabelConstants.KLASS_RELATIONSHIP) && !isInherited) {
          RelationshipFixup.inheritKlassRelationship(klassVertex, klassProperty);
        }
      }
    }
    
    UtilClass.getGraph()
        .commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Inherit_Non_Inheriting_Relationships/*" };
  }
}
