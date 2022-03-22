package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetEndpointsBySystemStrategy
    extends IConfigStrategy<IGetEnpointBySystemRequestModel, IGetGridEndpointsResponseModel> {
  
}
