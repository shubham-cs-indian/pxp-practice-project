package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDataStrategy
    extends IConfigStrategy<IGetConfigDataRequestModel, IGetConfigDataResponseModel> {
  
}
