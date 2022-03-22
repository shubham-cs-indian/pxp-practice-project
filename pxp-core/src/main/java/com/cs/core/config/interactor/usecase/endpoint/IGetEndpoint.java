package com.cs.core.config.interactor.usecase.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetEndpoint extends IGetConfigInteractor<IIdParameterModel, IGetEndpointForGridModel> {
  
}
