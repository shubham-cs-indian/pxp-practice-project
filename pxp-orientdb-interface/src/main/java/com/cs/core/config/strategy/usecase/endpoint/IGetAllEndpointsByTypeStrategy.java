package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IGetAllEndpointsByTypeRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IGetAllEndpointsByTypeStrategy
    extends IConfigStrategy<IGetAllEndpointsByTypeRequestModel, IListModel<IIdLabelCodeModel>> {
  
}
