package com.cs.core.config.interactor.model.asset;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class IconResponseModel extends IconModel implements IIconResponseModel {
  
  private static final long serialVersionUID = 1L;

  protected List<IAuditLogModel> auditLogInfo;
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    if (auditLogInfo == null) {
      auditLogInfo = new ArrayList<>();
    }
    return auditLogInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = AuditLogModel.class)
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
  
}
