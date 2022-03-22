package com.cs.core.config.interactor.model.variantcontext;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveVariantContextsResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveVariantContextsResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  
  protected IGetAllVariantContextsResponseModel success;
  IExceptionModel                               failure = new ExceptionModel();
  
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
  public IGetAllVariantContextsResponseModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = GetAllVariantContextsResponseModel.class)
  public void setSuccess(IGetAllVariantContextsResponseModel success)
  {
    this.success = success;
  }
}
