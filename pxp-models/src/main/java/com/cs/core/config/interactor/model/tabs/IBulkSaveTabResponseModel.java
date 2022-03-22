package com.cs.core.config.interactor.model.tabs;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveTabResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IGetGridTabsModel success);
}
