package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstancesRequestModel;

public interface IDeleteKlassInstances {
  
  public IDeleteInstancesResponseModel execute(IDeleteKlassInstancesRequestModel deleteRequest) throws Exception;
  
}
