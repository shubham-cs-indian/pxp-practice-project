package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class Orient_Migration_For_Process_Event_triggeringType_24_01_2019
    extends AbstractOrientMigration {
  
  public Orient_Migration_For_Process_Event_triggeringType_24_01_2019(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_Process_Event_triggeringType_24_01_2019/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    String query = "UPDATE Process_Event SET triggeringType = \'afterSave\' WHERE triggeringType IS NULL AND eventType = \'customevent\'";
    
    UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("message", "Success");
    return result;
  }
}
