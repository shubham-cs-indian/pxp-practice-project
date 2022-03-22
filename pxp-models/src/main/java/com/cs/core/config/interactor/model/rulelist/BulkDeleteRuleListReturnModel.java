package com.cs.core.config.interactor.model.rulelist;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteRuleListReturnModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeleteRuleListReturnModel {
  
  private static final long                 serialVersionUID = 1L;
  protected IExceptionModel                 failure;
  protected IBulkDeleteSuccessRuleListModel success;
  
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
  public IBulkDeleteSuccessRuleListModel getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(as = BulkDeleteSuccessRuleListModel.class)
  @Override
  public void setSuccess(IBulkDeleteSuccessRuleListModel model)
  {
    this.success = model;
  }
}
