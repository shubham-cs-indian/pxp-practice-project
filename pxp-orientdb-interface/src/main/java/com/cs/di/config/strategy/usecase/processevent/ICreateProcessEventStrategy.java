package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateProcessEventStrategy
    extends IConfigStrategy<IProcessEventModel, ICreateOrSaveProcessEventResponseModel> {
  
}
