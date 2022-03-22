package com.cs.core.config.strategy.model.governancerule;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteGovernanceRuleStrategyModel extends IBulkResponseModel {
  
  public void setSuccess(IBulkDeleteGovernanceRuleSuccessStrategyModel success);
}
