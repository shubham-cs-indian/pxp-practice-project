package com.cs.core.config.configdata;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;

public interface IGetConfigDataService extends IGetConfigService<IGetConfigDataRequestModel, IGetConfigDataResponseModel> {
  
}
