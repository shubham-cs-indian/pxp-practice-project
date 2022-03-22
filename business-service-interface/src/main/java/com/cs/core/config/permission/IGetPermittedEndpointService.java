package com.cs.core.config.permission;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;

public interface IGetPermittedEndpointService extends IGetConfigService<IGetConfigDataRequestModel, IGetConfigDataResponseModel> {
  
}
