package com.cs.core.config.interactor.model.endpoint;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveEndpointsResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveEndpointsResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected IExceptionModel                failure;
  protected IGetGridEndpointsResponseModel success;
  
  @Override
  public IGetGridEndpointsResponseModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridEndpointsResponseModel.class)
  @Override
  public void setSuccess(IGetGridEndpointsResponseModel success)
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
