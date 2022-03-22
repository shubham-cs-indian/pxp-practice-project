package com.cs.core.config.interactor.model.auditlog;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllAuditLogUserResponseModel implements IGetAllAuditLogUserResponseModel {

  protected Map<String, Object> auditLogUserList;
  protected Long                count;
  private static final long     serialVersionUID = 1L;
  
  @Override
  public Map<String, Object> getAuditLogUserList()
  {
    if (auditLogUserList == null) {
      auditLogUserList = new HashMap<String, Object>();
    }
    return auditLogUserList;
  }
  
  @Override
  @JsonDeserialize(contentAs = String.class)
  public void setAuditLogUserList(Map<String, Object> auditLogUserList)
  {
    this.auditLogUserList = auditLogUserList;
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
