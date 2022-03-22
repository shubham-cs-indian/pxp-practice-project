package com.cs.core.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ISaveEndpointStrategy
    extends IConfigStrategy<IListModel<ISaveEndpointModel>, IBulkSaveEndpointsResponseModel> {
  
}
