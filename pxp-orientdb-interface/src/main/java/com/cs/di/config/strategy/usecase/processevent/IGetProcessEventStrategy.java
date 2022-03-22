package com.cs.di.config.strategy.usecase.processevent;

import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetProcessEventStrategy
    extends IConfigStrategy<IIdParameterModel, IGetProcessEventModel> {
  
}
