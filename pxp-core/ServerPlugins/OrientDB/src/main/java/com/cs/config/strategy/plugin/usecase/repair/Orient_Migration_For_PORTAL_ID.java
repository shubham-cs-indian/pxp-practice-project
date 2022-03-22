package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Orient_Migration_For_PORTAL_ID extends AbstractOrientMigration {
  
  public Orient_Migration_For_PORTAL_ID(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_PORTAL_ID/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    
    List<String> entitiesToAddPortal = Arrays.asList(VertexLabelConstants.ORGANIZATION,
        VertexLabelConstants.ENTITY_TYPE_ROLE);
    String subQuery = entitiesToAddPortal.stream()
        .map(entity -> "(select from " + entity + ")")
        .collect(Collectors.joining(","));
    String query = "select from (select expand( UNIONALL(" + subQuery + "))) where "
        + IOrganization.PORTALS + " is null";
    Iterable<Vertex> verticesWithoutPortals = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    verticesWithoutPortals
        .forEach(vertex -> vertex.setProperty(IOrganization.PORTALS, new ArrayList<>()));
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
