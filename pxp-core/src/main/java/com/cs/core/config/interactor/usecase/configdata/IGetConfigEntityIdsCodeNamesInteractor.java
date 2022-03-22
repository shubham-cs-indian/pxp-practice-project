package com.cs.core.config.interactor.usecase.configdata;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesResponseModel;

public interface IGetConfigEntityIdsCodeNamesInteractor extends
    IGetConfigInteractor<IGetConfigEntityIdsCodeNamesRequestModel, IGetConfigEntityIdsCodeNamesResponseModel> {
  
}
