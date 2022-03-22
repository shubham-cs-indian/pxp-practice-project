package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTaskInstance extends
        IRuntimeInteractor<ITaskInstanceModel, IGetTaskInstanceModel> {
  
}
