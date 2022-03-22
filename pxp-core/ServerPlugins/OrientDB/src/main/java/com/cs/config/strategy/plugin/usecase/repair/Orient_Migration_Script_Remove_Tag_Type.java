package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class Orient_Migration_Script_Remove_Tag_Type extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Remove_Tag_Type(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    // TODO Auto-generated method stub
    return new String[] { "POST|Orient_Migration_Script_Remove_Tag_Type/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    
    String sqlQuery = "select from " + VertexLabelConstants.ENTITY_TAG
        + " where out('Child_Of').size() > 0";
    
    Iterable<Vertex> tagVertices = UtilClass.getGraph()
        .command(new OCommandSQL(sqlQuery))
        .execute();
    
    for (Vertex vertex : tagVertices) {
      vertex.removeProperty("tagType");
    }
    
    UtilClass.getGraph()
        .commit();
    
    return null;
  }
}
// { "id": "6",
// "pluginName":"Orient_Migration_Script_Remove_Tag_Type",
// "createdOnAsString":"22/08/2018",
// "description":"This migration script removes tagType key from all child Tag
// nodes."
// }
