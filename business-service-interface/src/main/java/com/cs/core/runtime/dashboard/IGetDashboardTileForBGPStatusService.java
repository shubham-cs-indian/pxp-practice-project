package com.cs.core.runtime.dashboard;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardBGPStatusResponseModel;

public interface IGetDashboardTileForBGPStatusService extends IRuntimeService<IDashboardBGPStatusRequestModel, IDashboardBGPStatusResponseModel> {
  
}
