package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteKlassInstanceStrategy
    extends IRuntimeStrategy<IDeleteKlassInstanceRequestModel, IDeleteInstancesResponseModel> {
  
}
