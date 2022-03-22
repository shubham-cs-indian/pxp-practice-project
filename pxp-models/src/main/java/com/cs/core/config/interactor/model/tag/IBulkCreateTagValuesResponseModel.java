package com.cs.core.config.interactor.model.tag;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkCreateTagValuesResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public IBulkCreateTagValuesSuccessModel getSuccess();
  
  public void setSuccess(IBulkCreateTagValuesSuccessModel success);
}
