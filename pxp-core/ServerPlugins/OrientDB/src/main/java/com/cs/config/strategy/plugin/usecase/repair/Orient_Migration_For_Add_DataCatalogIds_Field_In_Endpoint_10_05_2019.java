package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_For_Add_DataCatalogIds_Field_In_Endpoint_10_05_2019
    extends AbstractOrientMigration {
  
  public Orient_Migration_For_Add_DataCatalogIds_Field_In_Endpoint_10_05_2019(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] {
        "POST|Orient_Migration_For_Add_DataCatalogIds_Field_In_Endpoint_10_05_2019/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    List<String> defaultCatalogIds = Arrays.asList("dataIntegration");
    String updateQuery = "UPDATE Endpoint SET dataCatalogIds = "
        + EntityUtil.quoteIt(defaultCatalogIds);
    UtilClass.getGraph()
        .command(new OCommandSQL(updateQuery))
        .execute();
    UtilClass.getGraph()
        .commit();
    returnMap.put("Status", "Success");
    return returnMap;
  }
}
