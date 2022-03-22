package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkTaskInstanceResponseModel extends IBulkResponseModel {
  
  public ITaskInstanceResponseModel getSuccess();
  
  public void setSuccess(ITaskInstanceResponseModel success);
}
