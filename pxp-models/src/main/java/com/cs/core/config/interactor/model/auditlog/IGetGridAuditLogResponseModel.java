package com.cs.core.config.interactor.model.auditlog;

import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGridAuditLogResponseModel extends IModel {
  
  public static final String AUDIT_LOG_LIST = "auditLogList";
  public static final String COUNT          = "count";
  
  public List<IAuditLogModel> getAuditLogList();
  public void setAuditLogList(List<IAuditLogModel> auditLogList);
  
  public Long getCount();
  public void setCount(Long count);
  
}
