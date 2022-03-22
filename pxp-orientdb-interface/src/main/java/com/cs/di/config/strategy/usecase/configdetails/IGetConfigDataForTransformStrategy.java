package com.cs.di.config.strategy.usecase.configdetails;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformRequestModel;
import com.cs.di.config.interactor.model.configdetails.IGetConfigDataForTransformResponseModel;

public interface IGetConfigDataForTransformStrategy extends IConfigStrategy<IGetConfigDataForTransformRequestModel, IGetConfigDataForTransformResponseModel> {
  
}
