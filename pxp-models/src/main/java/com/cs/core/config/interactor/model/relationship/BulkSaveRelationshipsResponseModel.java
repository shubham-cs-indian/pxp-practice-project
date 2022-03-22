package com.cs.core.config.interactor.model.relationship;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkSaveRelationshipsResponseModel extends ConfigResponseWithAuditLogModel
    implements IBulkSaveRelationshipsResponseModel {
  
  private static final long                   serialVersionUID = 1L;
  
  protected IGetAllRelationshipsResponseModel success;
  protected IExceptionModel                   failure;
  
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
  public IGetAllRelationshipsResponseModel getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = GetAllRelationshipsResponseModel.class)
  public void setSuccess(IGetAllRelationshipsResponseModel success)
  {
    this.success = success;
  }
}
