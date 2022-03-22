package com.cs.core.config.strategy.usecase.dashboardtabs;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.dashboardtabs.DashboardTabResponseModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class CreateDashboardTabStrategy extends OrientDBBaseStrategy
    implements ICreateDashboardTabStrategy {
  
  @Override
  public IDashboardTabResponseModel execute(IDashboardTabModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.CREATE_DASHBOARD_TAB, model, DashboardTabResponseModel.class);
  }
}
