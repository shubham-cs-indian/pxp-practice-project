package com.cs.core.config.strategy.model.governancerule;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteKeyPerformanceIndexStrategyModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IBulkDeleteKeyPerformanceIndexSuccessStrategyModel success);
}
