package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Orient_Migration_FOR_CODE_COLLATE extends AbstractOrientMigration {
  
  public Orient_Migration_FOR_CODE_COLLATE(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_FOR_CODE_COLLATE/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    List<String> listOfVertexLabelConstants = Arrays.asList(
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, VertexLabelConstants.ENTITY_TAG,
        VertexLabelConstants.ENTITY_TYPE_ROLE, VertexLabelConstants.ENTITY_TYPE_USER,
        VertexLabelConstants.ENTITY_TYPE_TASK, VertexLabelConstants.DATA_RULE,
        VertexLabelConstants.PROPERTY_COLLECTION, VertexLabelConstants.VARIANT_CONTEXT,
        VertexLabelConstants.ROOT_RELATIONSHIP,
        VertexLabelConstants.DASHBOARD_TAB, VertexLabelConstants.GOVERNANCE_RULE_KPI,
        VertexLabelConstants.SYSTEM, VertexLabelConstants.UI_TRANSLATIONS,
        VertexLabelConstants.ENDPOINT, VertexLabelConstants.PROCESS_EVENT,
        VertexLabelConstants.PROPERTY_MAPPING);
    
    for (String vertexLabelConstant : listOfVertexLabelConstants) {
      String query = "ALTER PROPERTY " + vertexLabelConstant + "." + CommonConstants.CODE_PROPERTY
          + " COLLATE ci";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
