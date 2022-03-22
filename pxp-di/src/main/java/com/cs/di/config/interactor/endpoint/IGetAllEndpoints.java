package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;

public interface IGetAllEndpoints extends IGetConfigInteractor<IEndpointModel, IListModel<IEndpoint>> {
  
}
