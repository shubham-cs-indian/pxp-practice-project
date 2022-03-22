package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Tab_ProductVariantRelationship extends AbstractOrientPlugin {
  
  public Orient_Migration_Tab_ProductVariantRelationship(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|OrientMigrationTabProductVariantRelationship/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String query = "select expand(OUTE('" + RelationshipLabelConstants.HAS_TAB + "')) from "
        + VertexLabelConstants.NATURE_RELATIONSHIP + " where "
        + IKlassNatureRelationship.RELATIONSHIP_TYPE + "= '"
        + CommonConstants.PRODUCT_VARIANT_RELATIONSHIP + "'";
    
    Iterable<Edge> hasTabEdges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Vertex inVertex = UtilClass.getVertexById(SystemLevelIds.OVERVIEW_TAB, CommonConstants.TAB);
    
    for (Edge edge : hasTabEdges) {
      Vertex vertex = edge.getVertex(Direction.OUT);
      UtilClass.getGraph()
          .removeEdge(edge);
      vertex.addEdge(RelationshipLabelConstants.HAS_TAB, inVertex);
    }
    
    return null;
  }
}
