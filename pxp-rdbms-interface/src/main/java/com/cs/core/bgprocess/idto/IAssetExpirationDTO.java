package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAssetExpirationDTO extends ISimpleDTO{
  
  public Long getTimeStamp();
  public void setTimeStamp(Long timeStamp);
  
  public String getPhysicalCatalogId();
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getDataLanguage();
  public void setDataLanguage(String dataLanguage);
  
  public String getOrganizationId();
  public void setOrganizationId(String organizationId);
}
