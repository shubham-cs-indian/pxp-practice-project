package com.cs.core.config.interactor.usecase.configdata;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;

public interface IGetConfigData
    extends IGetConfigInteractor<IGetConfigDataRequestModel, IGetConfigDataResponseModel> {
  
}
