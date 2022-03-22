package com.cs.core.config.interactor.model.auditlog;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseModel;

public interface IGetAllAuditLogUserResponseModel extends IConfigResponseModel{
  
  public static final String AUDIT_LOG_USER_LIST = "auditLogUserList";
  public static final String COUNT          = "count";
  
  public Map<String, Object> getAuditLogUserList();
  public void setAuditLogUserList(Map<String, Object> auditLogList);
  
  public Long getCount();
  public void setCount(Long count);
  
}
