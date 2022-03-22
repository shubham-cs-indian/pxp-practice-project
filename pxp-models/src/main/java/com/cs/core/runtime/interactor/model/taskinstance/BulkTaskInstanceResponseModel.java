package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NONE)
public class BulkTaskInstanceResponseModel implements IBulkTaskInstanceResponseModel {
  
  private static final long            serialVersionUID = 1L;
  
  protected IExceptionModel            failure;
  protected ITaskInstanceResponseModel success;
  
  @Override
  public IExceptionModel getFailure()
  {
    if (failure == null) {
      failure = new ExceptionModel();
    }
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public ITaskInstanceResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = TaskInstanceResponseModel.class)
  @Override
  public void setSuccess(ITaskInstanceResponseModel success)
  {
    this.success = success;
  }
}
