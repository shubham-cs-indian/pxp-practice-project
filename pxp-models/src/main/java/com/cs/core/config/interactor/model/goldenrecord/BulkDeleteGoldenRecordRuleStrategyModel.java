package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteGoldenRecordRuleStrategyModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeleteGoldenRecordRuleStrategyModel {
  
  private static final long                                 serialVersionUID = 1L;
  
  protected IExceptionModel                                 failure;
  protected IBulkDeleteGoldenRecordRuleSuccessStrategyModel success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  @JsonDeserialize(as = ExceptionModel.class)
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Object getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(as = BulkDeleteGoldenRecordRuleSuccessStrategyModel.class)
  public void setSuccess(IBulkDeleteGoldenRecordRuleSuccessStrategyModel success)
  {
    this.success = success;
  }
}
