package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;

public interface IGetKlassInstanceIdsForRunningProcessesStrategy
    extends ICamundaStrategy<IDeleteKlassInstanceModel, IIdsListParameterModel> {
  
}
