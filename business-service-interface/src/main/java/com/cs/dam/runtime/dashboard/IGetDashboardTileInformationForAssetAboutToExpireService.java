package com.cs.dam.runtime.dashboard;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;

public interface IGetDashboardTileInformationForAssetAboutToExpireService extends
    IRuntimeService<IDashboardInformationRequestModel, IDashboardInformationResponseModel> {
}
