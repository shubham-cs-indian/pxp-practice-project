package com.cs.core.config.interactor.model.attribute;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveAttributeResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IBulkSaveAttributeSuccessModel success);
}
