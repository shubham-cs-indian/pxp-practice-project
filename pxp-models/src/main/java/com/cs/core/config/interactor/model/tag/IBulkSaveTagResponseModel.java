package com.cs.core.config.interactor.model.tag;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveTagResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IGetTagGridResponseModel success);
}
