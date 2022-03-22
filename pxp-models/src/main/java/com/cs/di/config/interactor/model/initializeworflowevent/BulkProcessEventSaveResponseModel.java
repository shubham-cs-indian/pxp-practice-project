package com.cs.di.config.interactor.model.initializeworflowevent;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkProcessEventSaveResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkProcessEventSaveResponseModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IExceptionModel                    failure;
  protected IGetGridProcessEventsResponseModel success;
  
  @Override
  public IGetGridProcessEventsResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridProcessEventsResponseModel.class)
  @Override
  public void setSuccess(IGetGridProcessEventsResponseModel success)
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
