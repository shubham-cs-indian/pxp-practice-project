package com.cs.core.config.strategy.usecase.dashboardtabs;

import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateDashboardTabStrategy
    extends IConfigStrategy<IDashboardTabModel, IDashboardTabResponseModel> {
  
}
