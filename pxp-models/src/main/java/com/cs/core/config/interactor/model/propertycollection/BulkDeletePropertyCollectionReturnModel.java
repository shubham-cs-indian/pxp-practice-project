package com.cs.core.config.interactor.model.propertycollection;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeletePropertyCollectionReturnModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeletePropertyCollectionReturnModel {
  
  private static final long                           serialVersionUID = 1L;
  protected IExceptionModel                           failure;
  protected IBulkDeleteSuccessPropertyCollectionModel success;
  
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
  public IBulkDeleteSuccessPropertyCollectionModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkDeleteSuccessPropertyCollectionModel.class)
  @Override
  public void setSuccess(IBulkDeleteSuccessPropertyCollectionModel model)
  {
    this.success = model;
  }
}
