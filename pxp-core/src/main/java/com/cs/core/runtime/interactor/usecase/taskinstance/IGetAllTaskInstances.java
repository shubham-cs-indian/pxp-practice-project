package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetAllTaskInstances extends IRuntimeInteractor<IIdParameterModel, IGetTaskInstanceForDashboardModel> {
  
}
