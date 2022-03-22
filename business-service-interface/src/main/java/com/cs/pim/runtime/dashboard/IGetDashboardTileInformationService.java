package com.cs.pim.runtime.dashboard;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;

public interface IGetDashboardTileInformationService extends
    IRuntimeService<IDashboardInformationRequestModel, IDashboardInformationResponseModel> {
}
