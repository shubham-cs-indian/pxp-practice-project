package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IDeleteProcessEventResponseModel;

public interface IDeleteProcessEventStrategy
    extends IConfigStrategy<IIdsListParameterModel, IDeleteProcessEventResponseModel> {
  
}
