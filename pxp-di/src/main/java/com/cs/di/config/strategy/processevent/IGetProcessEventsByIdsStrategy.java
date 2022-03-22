package com.cs.di.config.strategy.processevent;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetProcessEventsByIdsResponseModel;

public interface IGetProcessEventsByIdsStrategy
    extends IConfigStrategy<IIdsListParameterModel, IGetProcessEventsByIdsResponseModel> {
  
}
