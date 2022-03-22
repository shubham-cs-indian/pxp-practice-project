package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetProcessEventForDashboardStrategy extends IConfigStrategy<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>{
  
}
