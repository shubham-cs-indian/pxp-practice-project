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
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import java.util.HashMap;
import java.util.Map;

public class Orient_Remove_All_Klass_Sections_And_Properties extends AbstractOrientPlugin {
  
  public Orient_Remove_All_Klass_Sections_And_Properties(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> response = new HashMap<>();
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_KLASS);
    for (Vertex klassVertex : vertices) {
      Iterable<Vertex> sectionNodes = klassVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Vertex sectionVertex : sectionNodes) {
        sectionVertex.remove();
      }
      Iterable<Vertex> klassPropertyVertices = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassProperty : klassPropertyVertices) {
        if (((OrientVertex) klassProperty).getLabel()
            .equals(VertexLabelConstants.KLASS_RELATIONSHIP)) {
          // RelationshipFixup.inheritKlassRelationship(klassVertex,
          // klassProperty);
          continue;
        }
        klassProperty.remove();
      }
    }
    
    UtilClass.getGraph()
        .commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Remove_All_Klass_Sections_And_Properties/*" };
  }
}
