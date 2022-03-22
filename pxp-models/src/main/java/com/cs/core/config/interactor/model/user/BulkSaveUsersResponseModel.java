package com.cs.core.config.interactor.model.user;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveUsersResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveUsersResponseModel {
  
  private static final long            serialVersionUID = 1L;
  protected IGetGridUsersResponseModel success;
  protected IExceptionModel            failure;
  
  @Override
  public IGetGridUsersResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridUsersResponseModel.class)
  @Override
  public void setSuccess(IGetGridUsersResponseModel success)
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
