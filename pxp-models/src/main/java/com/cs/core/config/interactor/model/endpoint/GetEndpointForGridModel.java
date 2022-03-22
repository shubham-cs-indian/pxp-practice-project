package com.cs.core.config.interactor.model.endpoint;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetEndpointForGridModel extends GetEndpointModel implements IGetEndpointForGridModel {
  
  private static final long                     serialVersionUID = 1L;
  protected IConfigDetailsForGridEndpointsModel configDetails;
  protected List<IAuditLogModel>            auditLogInfo;
  
  @Override
  public IConfigDetailsForGridEndpointsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGridEndpointsModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGridEndpointsModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
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
