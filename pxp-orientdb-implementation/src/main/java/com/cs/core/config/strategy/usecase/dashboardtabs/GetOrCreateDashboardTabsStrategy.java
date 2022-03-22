package com.cs.core.config.strategy.usecase.dashboardtabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateDashboardTabsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateDashboardTabsStrategy {
  
  @Override
  public IListModel<IDashboardTabModel> execute(IListModel<IDashboardTabModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    execute(GET_OR_CREATE_DASHBOARD_TABS, requestMap);
    return null;
  }
}
