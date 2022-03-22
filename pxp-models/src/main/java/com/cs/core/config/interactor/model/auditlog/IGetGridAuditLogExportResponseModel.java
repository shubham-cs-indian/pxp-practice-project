package com.cs.core.config.interactor.model.auditlog;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGridAuditLogExportResponseModel extends IModel{
  
  public static final String AUDIT_LOG_EXPORT_LIST = "auditLogExportList";
  public static final String COUNT                 = "count";
  
  public Long getCount();
  public void setCount(Long count);
  
  public List<IAuditLogExportEntryModel> getAuditLogExportList();
  public void setAuditLogExportList(List<IAuditLogExportEntryModel> auditLogExportList);
}
