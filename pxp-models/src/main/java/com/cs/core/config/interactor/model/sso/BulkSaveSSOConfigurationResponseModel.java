package com.cs.core.config.interactor.model.sso;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveSSOConfigurationResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveSSOConfigurationResponseModel {
  
  private static final long                       serialVersionUID = 1L;
  protected IGetGridSSOConfigurationResponseModel success;
  protected IExceptionModel                       failure;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Object getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridSSOConfigurationResponseModel.class)
  @Override
  public void setSuccess(IGetGridSSOConfigurationResponseModel success)
  {
    this.success = success;
  }
}
