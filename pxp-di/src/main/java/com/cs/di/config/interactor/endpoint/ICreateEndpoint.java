package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;

public interface ICreateEndpoint extends ICreateConfigInteractor<IEndpointModel, IGetEndpointForGridModel> {
  
}
