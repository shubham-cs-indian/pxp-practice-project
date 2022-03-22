package com.cs.core.config.interactor.model.governancerule;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.attribute.IBulkSaveKpiRuleSuccessModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveKpiRuleResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IBulkSaveKpiRuleSuccessModel success);
}
