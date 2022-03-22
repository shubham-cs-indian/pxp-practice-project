package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface ICreateTaskStrategy extends IConfigStrategy<ITaskModel, IGetTaskModel> {
  
}
