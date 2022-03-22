package com.cs.core.runtime.taskinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.taskinstance.IBulkDeleteTaskInstanceReturnModel;
import com.cs.core.runtime.interactor.model.taskinstance.IDeleteTaskInstancesRequestModel;

public interface IDeleteTaskInstanceService extends IRuntimeService<IDeleteTaskInstancesRequestModel, IBulkDeleteTaskInstanceReturnModel> {
  
}
