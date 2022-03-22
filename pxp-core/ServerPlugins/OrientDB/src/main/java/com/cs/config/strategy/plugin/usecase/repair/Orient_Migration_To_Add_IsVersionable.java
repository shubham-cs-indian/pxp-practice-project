package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

public class Orient_Migration_To_Add_IsVersionable extends AbstractOrientMigration {
  
  public Orient_Migration_To_Add_IsVersionable(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_To_Add_IsVersionable/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    if (requestMap.isEmpty()) {
      String query = "Update " + CommonConstants.ATTRIBUTE + " SET isVersionable = true";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      query = "Update " + CommonConstants.TAG + " SET isVersionable = true";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    else {
      List<String> isVersionableAttributes = (List<String>) requestMap
          .get("isVersionableAttributes");
      List<String> isVersionableTags = (List<String>) requestMap.get("isVersionableTags");
      
      setIsVersionable(CommonConstants.ATTRIBUTE, isVersionableAttributes);
      setIsVersionable(CommonConstants.TAG, isVersionableTags);
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  private void setIsVersionable(String entity, List<String> isVersionableEntities)
  {
    String query = "Update " + entity + " SET isVersionable = false";
    UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    if (!isVersionableEntities.isEmpty()) {
      query = "Update " + entity + " SET isVersionable = true where code in "
          + EntityUtil.quoteIt(isVersionableEntities);
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
  }
}
