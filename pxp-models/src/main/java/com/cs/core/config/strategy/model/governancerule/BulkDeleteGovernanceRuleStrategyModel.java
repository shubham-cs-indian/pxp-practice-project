package com.cs.core.config.strategy.model.governancerule;

import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteGovernanceRuleStrategyModel
    implements IBulkDeleteGovernanceRuleStrategyModel {
  
  private static final long                               serialVersionUID = 1L;
  protected IExceptionModel                               failure;
  protected IBulkDeleteGovernanceRuleSuccessStrategyModel success;
  
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
  @JsonDeserialize(as = BulkDeleteGovernanceRuleSuccessStrategyModel.class)
  public void setSuccess(IBulkDeleteGovernanceRuleSuccessStrategyModel success)
  {
    this.success = success;
  }
}
