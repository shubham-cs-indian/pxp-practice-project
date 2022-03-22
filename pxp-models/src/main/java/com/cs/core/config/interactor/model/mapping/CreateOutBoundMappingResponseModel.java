package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;

public class CreateOutBoundMappingResponseModel extends OutBoundMappingModel
    implements ICreateOutBoundMappingResponseModel {
  
  private static final long serialVersionUID = 1L;
  List<IAuditLogModel>      auditLogInfo;
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    if (auditLogInfo == null) {
      auditLogInfo = new ArrayList<IAuditLogModel>();
    }
    return auditLogInfo;
  }
  
  @Override
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
}
