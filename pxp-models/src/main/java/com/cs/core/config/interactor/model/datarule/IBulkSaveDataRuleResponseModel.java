package com.cs.core.config.interactor.model.datarule;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveDataRuleResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(List<IBulkSaveDataRuleModel> success);
}
