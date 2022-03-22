package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.endpoint.IGetEnpointBySystemRequestModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;

public interface IGetEndpointsBySystemService extends IGetConfigService<IGetEnpointBySystemRequestModel, IGetGridEndpointsResponseModel> {
  
}
