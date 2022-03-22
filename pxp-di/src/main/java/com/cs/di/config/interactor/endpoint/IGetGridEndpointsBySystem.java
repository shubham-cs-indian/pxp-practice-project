package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;

public interface IGetGridEndpointsBySystem
    extends IGetConfigInteractor<IGetEnpointBySystemRequestModel, IGetGridEndpointsResponseModel> {
  
}
