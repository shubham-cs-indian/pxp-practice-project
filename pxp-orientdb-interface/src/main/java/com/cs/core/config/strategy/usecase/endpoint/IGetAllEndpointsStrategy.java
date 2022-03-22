package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllEndpointsStrategy
    extends IConfigStrategy<IEndpointModel, IListModel<IEndpoint>> {
  
}
