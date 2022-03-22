package com.cs.core.runtime.strategy.usecase.dashboard;

import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetDashboardTileRuleViolationInfoStrategy extends
    IRuntimeStrategy<IDashboardInformationRequestModel, IDashboardInformationResponseModel> {
  
}
