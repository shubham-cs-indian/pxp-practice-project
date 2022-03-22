package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTaskService extends IGetConfigService<IIdParameterModel, ITaskModel>{

}
