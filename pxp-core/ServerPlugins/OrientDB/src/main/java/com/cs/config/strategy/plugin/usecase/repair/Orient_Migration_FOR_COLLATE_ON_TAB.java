package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class Orient_Migration_FOR_COLLATE_ON_TAB extends AbstractOrientMigration {
  
  public Orient_Migration_FOR_COLLATE_ON_TAB(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_FOR_COLLATE_ON_TAB/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    
    String query = "ALTER PROPERTY " + VertexLabelConstants.TAB + "."
        + CommonConstants.CODE_PROPERTY + " COLLATE ci";
    UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (String lang : Constants.SUPPORTED_LANGUAGES) {
      query = "ALTER PROPERTY " + VertexLabelConstants.TAB + "." + CommonConstants.LABEL_PROPERTY
          + "__" + lang + " COLLATE ci";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
