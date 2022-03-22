package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class OrientMigrationForLimitedObjectDeprecation extends AbstractOrientPlugin {
  
  public OrientMigrationForLimitedObjectDeprecation(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|OrientMigrationForLimitedObjectDeprecation/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    // remove from variants
    graph.command(new OCommandSQL("Update " + VertexLabelConstants.VARIANT_CONTEXT + " remove isLimitedObject")).execute();
    //remove translations
    graph.command(new OCommandSQL("Delete vertex  " + VertexLabelConstants.UI_TRANSLATIONS + " where code = 'LIMITED_OBJECT'")).execute();
    graph.commit();
    return null;
  }
}
