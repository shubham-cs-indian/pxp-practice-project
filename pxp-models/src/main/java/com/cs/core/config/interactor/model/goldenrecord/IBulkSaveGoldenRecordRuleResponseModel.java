package com.cs.core.config.interactor.model.goldenrecord;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

public interface IBulkSaveGoldenRecordRuleResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(List<IIdLabelCodeModel> success);
}
