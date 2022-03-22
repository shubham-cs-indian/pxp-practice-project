package com.cs.core.config.interactor.model.task;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveTasksResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IGetGridTasksResponseModel success);
}
