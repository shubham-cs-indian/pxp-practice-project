package com.cs.core.config.interactor.model.sso;

import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICreateSSOConfigurationModel extends IConfigModel {
  
  public static final String ORGANIZATION_ID = "organizationId";
  public static final String DOMAIN          = "domain";
  public static final String IDP             = "idp";
  
  public String getDomain();
  
  public void setDomain(String domain);
  
  public String getIdp();
  
  public void setIdp(String idp);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getType();
  
  public void setType(String type);
}
