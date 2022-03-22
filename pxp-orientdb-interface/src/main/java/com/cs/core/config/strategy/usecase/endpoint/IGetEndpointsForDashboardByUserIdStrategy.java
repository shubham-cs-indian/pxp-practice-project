package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;

public interface IGetEndpointsForDashboardByUserIdStrategy
    extends IConfigStrategy<IDataIntegrationRequestModel, IGetEndointsInfoModel> {
  
}
