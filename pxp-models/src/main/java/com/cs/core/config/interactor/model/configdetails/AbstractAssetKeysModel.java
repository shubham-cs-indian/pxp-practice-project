package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.config.interactor.model.asset.IAssetKeysModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public abstract class AbstractAssetKeysModel implements IAssetKeysModel {
  
  private static final long     serialVersionUID = 1L;
  protected String              imageKey;
  protected String              thumbKey;
  protected Map<String, Object> metadata;
  protected String              hash;
  protected String              key;
  protected String              fileName;
  protected Boolean             searchResult;
  protected String              klassId;
  protected IExceptionModel     warnings;
  protected String              type;
  
  public AbstractAssetKeysModel(String imageKey, String thumbKey, Map<String, Object> metadata,
      String hash, String key, String fileName, String klassId)
  {
    this.imageKey = imageKey;
    this.thumbKey = thumbKey;
    this.metadata = metadata;
    this.hash = hash;
    this.key = key;
    this.fileName = fileName;
    this.klassId = klassId;
  }
  
  @Override
  public String getImageKey()
  {
    return imageKey;
  }
  
  @Override
  public void setImageKey(String imageKey)
  {
    this.imageKey = imageKey;
  }
  
  @Override
  public String getThumbKey()
  {
    return thumbKey;
  }
  
  @Override
  public void setThumbKey(String thumbKey)
  {
    this.thumbKey = thumbKey;
  }
  
  @Override
  public Map<String, Object> getMetadata()
  {
    return metadata;
  }
  
  @Override
  public void setMetadata(Map<String, Object> metadata)
  {
    this.metadata = metadata;
  }
  
  @Override
  public String getHash()
  {
    return hash;
  }
  
  @Override
  public void setHash(String hash)
  {
    this.hash = hash;
  }
  
  @Override
  public String getKey()
  {
    return key;
  }
  
  @Override
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public String getFileName()
  {
    return fileName;
  }
  
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  public Boolean getSearchResult()
  {
    return searchResult;
  }
  
  public void setSearchResult(Boolean searchResult)
  {
    this.searchResult = searchResult;
  }
  
  public String getKlassId()
  {
    return klassId;
  }
  
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
  @Override
  public IExceptionModel getWarnings()
  {
    return warnings;
  }
  
  @Override
  public void setWarnings(IExceptionModel warnings)
  {
    this.warnings = warnings;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
