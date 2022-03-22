package com.cs.core.config.interactor.usecase.endpoint;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;

public interface IGetPermittedEndpoint
    extends IGetConfigInteractor<IGetConfigDataRequestModel, IGetConfigDataResponseModel> {
  
}
