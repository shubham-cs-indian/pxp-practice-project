package com.cs.core.config.strategy.usecase.dashboardtabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateDashboardTabsStrategy
    extends IConfigStrategy<IListModel<IDashboardTabModel>, IListModel<IDashboardTabModel>> {
  
}
