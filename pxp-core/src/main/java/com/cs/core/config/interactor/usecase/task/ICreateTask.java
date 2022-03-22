package com.cs.core.config.interactor.usecase.task;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ICreateTask extends ICreateConfigInteractor<ITaskModel, IGetTaskModel> {
  
}
