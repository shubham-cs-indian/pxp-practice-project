package com.cs.core.runtime.interactor.usecase.dashboard;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetDashboardTileForBGPStatus extends
    IRuntimeInteractor<IDashboardBGPStatusRequestModel, IDashboardBGPStatusResponseModel> {
  
}
