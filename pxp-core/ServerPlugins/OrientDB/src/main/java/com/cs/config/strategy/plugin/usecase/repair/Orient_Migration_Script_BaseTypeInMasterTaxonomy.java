package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class Orient_Migration_Script_BaseTypeInMasterTaxonomy extends AbstractOrientMigration {
  
  public Orient_Migration_Script_BaseTypeInMasterTaxonomy(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_BaseTypeInMasterTaxonomy/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    
    String sqlQuery = "select from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY;
    
    Iterable<Vertex> masterTaxonomyVertices = UtilClass.getGraph()
        .command(new OCommandSQL(sqlQuery))
        .execute();
    
    for (Vertex vertex : masterTaxonomyVertices) {
      if (vertex.getProperty("baseType") == null) {
        vertex.setProperty("baseType",
            "com.cs.config.interactor.entity.attributiontaxonomy.MasterTaxonomy");
      }
    }    
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
