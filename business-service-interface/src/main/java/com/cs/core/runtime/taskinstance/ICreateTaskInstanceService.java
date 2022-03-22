package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceModel;

public interface ICreateTaskInstanceService extends IRuntimeService<ITaskInstanceModel, IGetTaskInstanceModel> {
  
}
