package com.cs.core.config.interactor.model.tag;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveTagResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveTagResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected IGetTagGridResponseModel success;
  protected IExceptionModel          failure;
  
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
  public IGetTagGridResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetTagGridResponseModel.class)
  @Override
  public void setSuccess(IGetTagGridResponseModel success)
  {
    this.success = success;
  }
}
