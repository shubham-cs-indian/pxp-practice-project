package com.cs.core.config.interactor.model.template;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveTemplatesResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveTemplatesResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected IBulkSaveTemplatesSuccessModel success;
  protected IExceptionModel                failure;
  
  @Override
  public IBulkSaveTemplatesSuccessModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkSaveTemplatesSuccessModel.class)
  @Override
  public void setSuccess(IBulkSaveTemplatesSuccessModel success)
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
