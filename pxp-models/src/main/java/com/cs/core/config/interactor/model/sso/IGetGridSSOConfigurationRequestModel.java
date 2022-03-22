package com.cs.core.config.interactor.model.sso;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface IGetGridSSOConfigurationRequestModel extends IConfigModel {
  
  public static final String OrganizationId = "organizationId";
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
}
