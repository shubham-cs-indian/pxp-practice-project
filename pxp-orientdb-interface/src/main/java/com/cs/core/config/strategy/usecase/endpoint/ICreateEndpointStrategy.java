package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateEndpointStrategy
    extends IConfigStrategy<IEndpointModel, IGetEndpointForGridModel> {
  
}
