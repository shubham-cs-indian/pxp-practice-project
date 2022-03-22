package com.cs.core.config.interactor.model.sso;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridSSOConfigurationResponseModel extends IModel, IConfigResponseWithAuditLogModel {
  
  public static final String SSO_CONFIGURATION_LIST = "ssoList";
  public static final String COUNT                  = "count";
  
  public List<ISSOConfigurationModel> getSsoList();
  
  public void setSsoList(List<ISSOConfigurationModel> ssoList);
  
  public Long getCount();
  
  public void setCount(Long count);
}
