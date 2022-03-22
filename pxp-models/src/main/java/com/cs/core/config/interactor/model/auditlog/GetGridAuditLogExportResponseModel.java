package com.cs.core.config.interactor.model.auditlog;

import java.util.List;

public class GetGridAuditLogExportResponseModel implements IGetGridAuditLogExportResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  
  protected Long                            count;
  protected List<IAuditLogExportEntryModel> auditLogExportList;
  
  @Override
  public List<IAuditLogExportEntryModel> getAuditLogExportList()
  {
    return auditLogExportList;
  }
  
  @Override
  public void setAuditLogExportList(List<IAuditLogExportEntryModel> auditLogExportList)
  {
    this.auditLogExportList = auditLogExportList;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
}
