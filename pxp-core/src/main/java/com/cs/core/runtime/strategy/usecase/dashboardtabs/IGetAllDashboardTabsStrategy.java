package com.cs.core.runtime.strategy.usecase.dashboardtabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.IGetAllDashboardTabsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetAllDashboardTabsStrategy
    extends IConfigStrategy<IGetAllDashboardTabsRequestModel, IListModel<IIdLabelCodeModel>> {
  
}
