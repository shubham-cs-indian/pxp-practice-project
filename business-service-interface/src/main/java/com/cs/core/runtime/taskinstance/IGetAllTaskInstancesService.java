package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceForDashboardModel;

public interface IGetAllTaskInstancesService extends IRuntimeService<IIdParameterModel, IGetTaskInstanceForDashboardModel> {
  
}
