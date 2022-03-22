package com.cs.core.config.interactor.model.rulelist;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkDeleteRuleListReturnModel
    extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static String SUCCESS = "success";
  
  public void setSuccess(IBulkDeleteSuccessRuleListModel model);
}
