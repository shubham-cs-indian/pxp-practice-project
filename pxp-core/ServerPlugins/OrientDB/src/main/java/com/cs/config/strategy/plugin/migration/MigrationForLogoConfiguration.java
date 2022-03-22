package com.cs.config.strategy.plugin.migration;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class MigrationForLogoConfiguration extends AbstractOrientPlugin {

  public MigrationForLogoConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationForLogoConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "Select From " + VertexLabelConstants.LOGO_CONFIGURATION;
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query)).execute();
    
    for(Vertex vertex : vertices) {
      vertex.removeProperty("isLandingPageExpanded");
      vertex.removeProperty("isProductInfoPageExpanded");
      vertex.removeProperty("auditLogInfo");
    }
    graph.commit();
    return null;
  }
  
  
}
