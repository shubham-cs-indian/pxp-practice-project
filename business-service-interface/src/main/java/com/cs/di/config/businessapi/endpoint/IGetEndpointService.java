package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetEndpointService extends IGetConfigService<IIdParameterModel, IGetEndpointForGridModel> {
  
}
