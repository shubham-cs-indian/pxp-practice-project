package com.cs.core.config.interactor.model.auditlog;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetGridAuditLogResponseModel implements IGetGridAuditLogResponseModel {
  
  protected List<IAuditLogModel> auditLogList;
  protected Long                     count;
  private static final long          serialVersionUID = 1L;
  
  @Override
  public List<IAuditLogModel> getAuditLogList()
  {
    if (auditLogList == null) {
      auditLogList = new ArrayList<>();
    }
    return auditLogList;
  }
  
  @Override
  @JsonDeserialize(contentAs = AuditLogModel.class)
  public void setAuditLogList(List<IAuditLogModel> auditLogList)
  {
    this.auditLogList = auditLogList;
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
