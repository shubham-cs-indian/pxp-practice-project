package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.config.interactor.model.task.ITaskModel;

public interface ICreateTaskService extends ICreateConfigService<ITaskModel, IGetTaskModel> {

}
