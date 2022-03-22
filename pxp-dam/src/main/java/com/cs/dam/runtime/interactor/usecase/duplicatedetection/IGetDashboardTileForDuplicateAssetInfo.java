package com.cs.dam.runtime.interactor.usecase.duplicatedetection;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetDashboardTileForDuplicateAssetInfo extends
    IRuntimeInteractor<IDashboardInformationRequestModel, IDashboardInformationResponseModel> {
  
}
