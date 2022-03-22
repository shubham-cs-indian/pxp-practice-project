package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;


public interface IBulkUploadIconsDTO extends ISimpleDTO {
  
  public List<IAssetFileDTO> getAssets();
  public void setAssets(List<IAssetFileDTO> assets);
  
  public String getLocaleId();
  public void setLocaleId(String localeId);
  
  public String getUserId();
  public void setUserId(String userId);
  
  public String getOrganizationCode();
  public void setOrganizationCode(String organizationCode);
  
}
