package com.cs.config.strategy.plugin.usecase.dashboardtabs;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Map;

public class CreateDashboardTab extends AbstractOrientPlugin {
  
  public CreateDashboardTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateDashboardTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex tabNode = TabUtils.createDashboardTabNode(requestMap, new ArrayList<>());
    UtilClass.getGraph()
        .commit();
    Map<String, Object> responseMap = UtilClass.getMapFromVertex(new ArrayList<>(), tabNode);
    
    return responseMap;
  }
}
