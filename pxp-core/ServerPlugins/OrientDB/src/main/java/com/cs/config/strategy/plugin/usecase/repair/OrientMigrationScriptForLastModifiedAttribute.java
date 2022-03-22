package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientMigrationScriptForLastModifiedAttribute extends AbstractOrientMigration {
  
  public OrientMigrationScriptForLastModifiedAttribute(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|MigrationToDisableVersioningForLastModifiedAttributes/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    String query = getAttributesQuery();
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> attributes = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex attribute : attributes) {
      attribute.setProperty(IAttribute.IS_VERSIONABLE, false);
    }
    graph.commit();
    return attributes;
  }
  
  protected String getAttributesQuery()
  {
    String query = "select from " + VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE
        + " where code in ['" + IStandardConfig.StandardProperty.lastmodifiedattribute.toString()
        + "', '" + IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString() + "']";
    return query;
  }
}
