package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetOrCreateEndPointStrategy
    extends IConfigStrategy<IListModel<IEndpoint>, IModel> {
  
}
