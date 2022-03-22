package com.cs.core.config.interactor.model.relationship;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveRelationshipsResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IGetAllRelationshipsResponseModel success);
}
