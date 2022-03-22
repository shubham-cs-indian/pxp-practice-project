package com.cs.core.config.interactor.model.entity.hidden;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveEntityPropertyResponseModel extends IdParameterModel
    implements ISaveEntityPropertyResponseModel {
  
  private static final long          serialVersionUID = 1L;
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
