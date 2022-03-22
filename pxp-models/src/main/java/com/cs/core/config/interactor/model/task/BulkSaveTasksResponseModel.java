package com.cs.core.config.interactor.model.task;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveTasksResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveTasksResponseModel {
  
  private static final long            serialVersionUID = 1L;
  protected IGetGridTasksResponseModel success;
  protected IExceptionModel            failure;
  
  @Override
  public IGetGridTasksResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridTasksResponseModel.class)
  @Override
  public void setSuccess(IGetGridTasksResponseModel success)
  {
    this.success = success;
  }
  
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
}
