package com.cs.core.runtime.interactor.usecase.dashboard;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetDashboardTileInformation extends
    IRuntimeInteractor<IDashboardInformationRequestModel, IDashboardInformationResponseModel> {
}
