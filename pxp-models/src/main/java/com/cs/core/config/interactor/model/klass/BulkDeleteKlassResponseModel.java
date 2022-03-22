package com.cs.core.config.interactor.model.klass;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteKlassResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeleteKlassResponseModel {
  
  private static final long            serialVersionUID = 1L;
  IExceptionModel                      failure;
  IBulkDeleteSuccessKlassResponseModel success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    if (failure == null) {
      failure = new ExceptionModel();
    }
    this.failure = failure;
  }
  
  @Override
  public IBulkDeleteSuccessKlassResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkDeleteSuccessKlassResponseModel.class)
  @Override
  public void setSuccess(IBulkDeleteSuccessKlassResponseModel model)
  {
    this.success = model;
  }
}
