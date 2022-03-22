package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteReturnModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static String SUCCESS = "success";
  
  public void setSuccess(List<String> ids);
}
