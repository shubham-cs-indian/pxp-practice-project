package com.cs.core.config.interactor.usecase.objectCount;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.objectCount.IGetConfigEntityResponseModel;
import com.cs.core.config.interactor.model.objectCount.IGetEntityTypeRequestModel;

public interface IGetEnityCount extends IGetConfigInteractor<IGetEntityTypeRequestModel, IGetConfigEntityResponseModel>{
  
}
