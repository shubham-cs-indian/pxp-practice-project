package com.cs.core.config.strategy.model.governancerule;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteKeyPerformanceIndexStrategyModel extends ConfigResponseWithAuditLogModel
    implements IBulkDeleteKeyPerformanceIndexStrategyModel {
  
  private static final long                                    serialVersionUID = 1L;
  protected IExceptionModel                                    failure;
  protected IBulkDeleteKeyPerformanceIndexSuccessStrategyModel success;
  
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
  @JsonDeserialize(as = BulkDeleteKeyPerformanceIndexSuccessStrategyModel.class)
  public void setSuccess(IBulkDeleteKeyPerformanceIndexSuccessStrategyModel success)
  {
    this.success = success;
  }
}
