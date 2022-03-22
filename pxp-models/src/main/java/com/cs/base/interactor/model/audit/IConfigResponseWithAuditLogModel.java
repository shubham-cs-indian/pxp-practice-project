package com.cs.base.interactor.model.audit;

import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;

public interface IConfigResponseWithAuditLogModel extends IConfigResponseModel {
  
  public static final String AUDIT_LOG_INFO = "auditLogInfo";
  
  public List<IAuditLogModel> getAuditLogInfo();
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo);
  
}
