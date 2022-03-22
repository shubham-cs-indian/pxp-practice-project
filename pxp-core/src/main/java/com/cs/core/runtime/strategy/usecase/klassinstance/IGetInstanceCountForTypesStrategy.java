package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.config.interactor.model.klass.IInstanceCountRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IInstancesCountResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetInstanceCountForTypesStrategy
    extends IRuntimeStrategy<IInstanceCountRequestModel, IInstancesCountResponseModel> {
  
}
