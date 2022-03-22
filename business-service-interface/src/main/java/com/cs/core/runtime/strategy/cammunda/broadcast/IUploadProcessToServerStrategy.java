package com.cs.core.runtime.strategy.cammunda.broadcast;

import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.ICamundaStrategy;

public interface IUploadProcessToServerStrategy
    extends ICamundaStrategy<IProcessEventModel, IModel> {
  
}
