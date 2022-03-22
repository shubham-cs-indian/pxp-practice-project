package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IInitializeBGProcessDTO extends ISimpleDTO {
  
  public String getCatalogCode();
  
  public void setCatalogCode(String code);
  
  public String getLocaleID();
  
  public void setLocaleID(String localeID);
  
  public String getOrganizationCode();
  
  public void setOrganizationCode(String organizationCode);
  
  public void setUserId(String userId);
  
  public String getUserId();
  
  public void setUserName(String userName);
  
  public String getUserName();
}
