package com.cs.core.config.interactor.model.mapping;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveMappingsResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public void setSuccess(IGetAllMappingsResponseModel success);
}
