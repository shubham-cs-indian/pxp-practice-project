package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class Orient_Migration_Script_Data_Rule extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Data_Rule(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Data_Rule/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Iterable<Vertex> dataRules = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.DATA_RULE);
    
    for (Vertex dataRule : dataRules) {
      dataRule.setProperty(IDataRule.IS_LANGUAGE_DEPENDENT, false);
    }
    
    UtilClass.getGraph()
        .commit();
    
    return null;
  }
}
