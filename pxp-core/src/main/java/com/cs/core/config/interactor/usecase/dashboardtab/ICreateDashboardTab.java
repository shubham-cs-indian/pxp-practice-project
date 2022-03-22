package com.cs.core.config.interactor.usecase.dashboardtab;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabResponseModel;

public interface ICreateDashboardTab
    extends ICreateConfigInteractor<IDashboardTabModel, IDashboardTabResponseModel> {
  
}
