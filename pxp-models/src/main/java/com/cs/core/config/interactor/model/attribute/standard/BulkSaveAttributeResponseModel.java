package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeSuccessModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

  public class BulkSaveAttributeResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveAttributeResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected IBulkSaveAttributeSuccessModel success;
  protected IExceptionModel                failure;
  
  @Override
  public IBulkSaveAttributeSuccessModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkSaveAttributeSuccessModel.class)
  @Override
  public void setSuccess(IBulkSaveAttributeSuccessModel success)
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
