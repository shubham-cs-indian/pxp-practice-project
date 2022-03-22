package com.cs.core.runtime.interactor.usecase.taskinstance;

import com.cs.core.runtime.interactor.model.taskinstance.IBulkDeleteTaskInstanceReturnModel;
import com.cs.core.runtime.interactor.model.taskinstance.IDeleteTaskInstancesRequestModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IDeleteTaskInstance extends
IRuntimeInteractor<IDeleteTaskInstancesRequestModel, IBulkDeleteTaskInstanceReturnModel>  {
  
}
