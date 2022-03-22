package com.cs.core.config.interactor.model.sso;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetGridSSOConfigurationResponseModel extends ConfigResponseWithAuditLogModel implements IGetGridSSOConfigurationResponseModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected List<ISSOConfigurationModel> ssoList;
  protected Long                         count;
  
  @Override
  public List<ISSOConfigurationModel> getSsoList()
  {
    return ssoList;
  }
  
  @JsonDeserialize(contentAs = SSOConfigurationModel.class)
  @Override
  public void setSsoList(List<ISSOConfigurationModel> ssoList)
  {
    this.ssoList = ssoList;
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
