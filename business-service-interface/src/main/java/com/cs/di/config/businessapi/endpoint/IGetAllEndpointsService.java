package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;

public interface IGetAllEndpointsService extends IGetConfigService<IEndpointModel, IListModel<IEndpoint>> {
  
}
