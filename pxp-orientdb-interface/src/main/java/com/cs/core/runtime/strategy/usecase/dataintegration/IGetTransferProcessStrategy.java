package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTransferProcessStrategy
    extends IConfigStrategy<IIdParameterModel, IGetProcessEventModel> {
  
}
