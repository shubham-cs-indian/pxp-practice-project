package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllEndpointIndexesStrategy
    extends IConfigStrategy<IEndpointModel, IIdsListParameterModel> {
  
}
