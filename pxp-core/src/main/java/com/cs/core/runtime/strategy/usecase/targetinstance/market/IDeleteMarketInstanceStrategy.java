package com.cs.core.runtime.strategy.usecase.targetinstance.market;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteMarketInstanceStrategy extends
IRuntimeStrategy<IIdsListParameterModel, IDeleteInstancesResponseModel>{
  
}
