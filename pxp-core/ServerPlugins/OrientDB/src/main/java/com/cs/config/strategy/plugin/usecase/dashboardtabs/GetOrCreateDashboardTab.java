package com.cs.config.strategy.plugin.usecase.dashboardtabs;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.dashboardtabs.IDashboardTab;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateDashboardTab extends AbstractOrientPlugin {
  
  public GetOrCreateDashboardTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateDashboardTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get("list");
    
    for (Map<String, Object> map : list) {
      String id = (String) map.get(IDashboardTab.ID);
      try {
        UtilClass.getVertexById(id, VertexLabelConstants.DASHBOARD_TAB);
        continue; // It is already created. Don't try to create it again
      }
      catch (NotFoundException e) {
        // Do nothing
      }
      TabUtils.createDashboardTabNode(map, new ArrayList<>());
    }
    UtilClass.getGraph()
        .commit();
    return new HashMap<String, Object>();
  }
}
