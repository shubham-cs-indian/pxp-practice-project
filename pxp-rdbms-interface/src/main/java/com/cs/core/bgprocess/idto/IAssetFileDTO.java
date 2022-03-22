package com.cs.core.bgprocess.idto;

import java.util.List;
import java.util.Map;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAssetFileDTO extends ISimpleDTO {
  
  public void setKlassID(String klassID);
  public String getKlassID();
  
  public void setFilePath(String fileName);
  public String getFilePath();
  
  public Map<String, Object> getMetaData();
  public void setMetaData(Map<String, Object> metaData);

  public String getHash();
  public void setHash(String hash);
  
  public String getExtensionType();
  public void setExtensionType(String extensionType);
  
  public Boolean getIsExtracted();
  public void setIsExtracted(Boolean isExtracted);
  
  public Map<String, Object> getExtensionConfiguration();
  public void setExtensionConfiguration(Map<String, Object> extensionConfiguration);
  
  public String getCode();
  public void setCode(String code);
  
  public String getName();
  public void setName(String name);
  
  public List<String> getIds();
  public void setIds(List<String> ids);
  
  public boolean getIsInDesignServerEnabled();
  public void setIsInDesignServerEnabled(boolean isInDesignServerEnabled);
}
