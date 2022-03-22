package com.cs.core.runtime.strategy.apis.klassinstance;

import com.cs.core.config.interactor.model.klass.IInstanceCountRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IInstancesCountResponseModel;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetInstanceCountForTypesStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetInstanceCountForTypesStrategy implements IGetInstanceCountForTypesStrategy {
  
  @Override
  public IInstancesCountResponseModel execute(IInstanceCountRequestModel model) throws Exception
  {
    return null;
  }
}
