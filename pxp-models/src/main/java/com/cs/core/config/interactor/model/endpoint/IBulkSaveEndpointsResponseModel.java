package com.cs.core.config.interactor.model.endpoint;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkSaveEndpointsResponseModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public IGetGridEndpointsResponseModel getSuccess();
  
  public void setSuccess(IGetGridEndpointsResponseModel success);
}
