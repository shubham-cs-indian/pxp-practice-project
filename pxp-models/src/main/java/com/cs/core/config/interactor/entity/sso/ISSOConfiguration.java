package com.cs.core.config.interactor.entity.sso;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;

public interface ISSOConfiguration extends IConfigMasterPropertyEntity {
  
  public static final String DOMAIN = "domain";
  public static final String IDP    = "idp";
  
  public String getDomain();
  
  public void setDomain(String domain);
  
  public String getIdp();
  
  public void setIdp(String idp);
}
