package com.cs.core.config.interactor.model.tag;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkCreateTagValuesResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkCreateTagValuesResponseModel {
  
  private static final long        serialVersionUID = 1L;
  IBulkCreateTagValuesSuccessModel success;
  IExceptionModel                  failure;
  
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
  public IBulkCreateTagValuesSuccessModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkCreateTagValuesSuccessModel.class)
  @Override
  public void setSuccess(IBulkCreateTagValuesSuccessModel success)
  {
    this.success = success;
  }
}
