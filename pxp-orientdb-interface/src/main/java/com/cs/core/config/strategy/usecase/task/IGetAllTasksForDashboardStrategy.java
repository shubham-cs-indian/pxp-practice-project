package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetConfigDetailsForTasksDashboardModel;

public interface IGetAllTasksForDashboardStrategy
    extends IConfigStrategy<IIdParameterModel, IGetConfigDetailsForTasksDashboardModel> {
  
}
