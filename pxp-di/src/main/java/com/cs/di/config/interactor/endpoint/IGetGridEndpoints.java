package com.cs.di.config.interactor.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetGridEndpoints
    extends IGetConfigInteractor<IConfigGetAllRequestModel, IGetGridEndpointsResponseModel> {
  
}
