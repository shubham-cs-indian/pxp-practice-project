package com.cs.core.config.interactor.model.klass;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteKlassResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static String SUCCESS = "success";
  
  public void setSuccess(IBulkDeleteSuccessKlassResponseModel model);
}
