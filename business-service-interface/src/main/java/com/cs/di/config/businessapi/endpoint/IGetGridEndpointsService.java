package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetGridEndpointsService extends IGetConfigService<IConfigGetAllRequestModel, IGetGridEndpointsResponseModel> {
  
}
