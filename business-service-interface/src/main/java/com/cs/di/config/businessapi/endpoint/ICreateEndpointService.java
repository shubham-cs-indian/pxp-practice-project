package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;

public interface ICreateEndpointService extends ICreateConfigService<IEndpointModel, IGetEndpointForGridModel> {
  
}
