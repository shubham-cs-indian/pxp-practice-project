package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceModel;
import com.cs.core.runtime.interactor.model.taskinstance.ISaveTaskInstanceModel;

public interface ISaveTaskInstanceService extends IRuntimeService<ISaveTaskInstanceModel, IGetTaskInstanceModel> {
  
}
