package com.cs.core.config.interactor.model.mapping;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveMappingsResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveMappingsResponseModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected IGetAllMappingsResponseModel success;
  protected IExceptionModel              failure;
  
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
  public IGetAllMappingsResponseModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = GetAllMappingsResponseModel.class)
  public void setSuccess(IGetAllMappingsResponseModel success)
  {
    this.success = success;
  }
}
