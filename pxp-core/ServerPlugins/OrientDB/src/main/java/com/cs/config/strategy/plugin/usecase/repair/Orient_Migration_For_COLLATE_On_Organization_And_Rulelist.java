package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Map;

public class Orient_Migration_For_COLLATE_On_Organization_And_Rulelist
    extends AbstractOrientMigration {
  
  public Orient_Migration_For_COLLATE_On_Organization_And_Rulelist(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_For_COLLATE_On_Organization_And_Rulelist/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    
    applyCollate(VertexLabelConstants.ORGANIZATION);
    applyCollate(VertexLabelConstants.RULE_LIST);
    
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  private void applyCollate(String vertexLabel)
  {
    OrientVertexType vertexType = UtilClass.getGraph()
        .getVertexType(vertexLabel);
    OProperty property = vertexType.getProperty(CommonConstants.CODE_PROPERTY);
    if (property == null) {
      UtilClass.createPropertyAndApplyFullTextIndex(vertexType, CommonConstants.CODE_PROPERTY);
    }
    else {
      String query = "ALTER PROPERTY " + vertexLabel + "." + CommonConstants.CODE_PROPERTY
          + " COLLATE ci";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    
    for (String lang : Constants.SUPPORTED_LANGUAGES) {
      String query = "ALTER PROPERTY " + vertexLabel + "." + CommonConstants.LABEL_PROPERTY + "__"
          + lang + " COLLATE ci";
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
  }
}
