package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteGoldenRecordRuleStrategyModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IBulkDeleteGoldenRecordRuleSuccessStrategyModel success);
}
