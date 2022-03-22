package com.cs.core.config.interactor.model.tabs;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveTabResponseModel extends ConfigResponseWithAuditLogModel implements IBulkSaveTabResponseModel {
  
  private static final long serialVersionUID = 1L;
  IGetGridTabsModel         success;
  IExceptionModel           failure;
  
  @Override
  public IGetGridTabsModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = GetGridTabsModel.class)
  @Override
  public void setSuccess(IGetGridTabsModel success)
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
