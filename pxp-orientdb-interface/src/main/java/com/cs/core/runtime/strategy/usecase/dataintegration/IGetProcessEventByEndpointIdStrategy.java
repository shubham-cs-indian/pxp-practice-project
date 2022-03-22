package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetProcessEventByEndpointIdStrategy
    extends IConfigStrategy<IIdParameterModel, IGetProcessEndpointEventModel> {
  
}
