package com.cs.config.strategy.plugin.migration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class DropIndexesOfPropertyVertexType extends AbstractOrientPlugin {
  
  public DropIndexesOfPropertyVertexType(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DropIndexesOfPropertyVertexType/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getGraph()
        .commit();
    UtilClass.getDatabase()
        .commit();
    
    UtilClass.getGraph()
        .command(new OCommandSQL("DROP index " + VertexLabelConstants.ENTITY_TYPE_PROPERTY + "."
            + CommonConstants.CODE_PROPERTY))
        .execute();
    
    UtilClass.getGraph()
        .command(new OCommandSQL("DROP index " + VertexLabelConstants.ENTITY_TYPE_PROPERTY + "."
            + CommonConstants.CODE_PROPERTY))
        .execute();
    
    return null;
  }
}
