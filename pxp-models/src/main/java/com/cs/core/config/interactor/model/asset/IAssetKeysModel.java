package com.cs.core.config.interactor.model.asset;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;

public interface IAssetKeysModel extends IModel {
  
  public static final String IMAGE_KEY     = "imageKey";
  public static final String THUMB_KEY     = "thumbKey";
  public static final String METADATA      = "metadata";
  public static final String HASH          = "hash";
  public static final String KEY           = "key";
  public static final String SEARCH_RESULT = "searchResult";
  public static final String KLASS_ID      = "klassId";
  public static final String WARNINGS      = "warnings";
  public static final String TYPE          = "type";
  public static final String FILE_NAME     = "fileName";
  
  public String getImageKey();
  
  public void setImageKey(String imageKey);
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
  
  public String getHash();
  
  public void setHash(String hash);
  
  public String getKey();
  
  public void setKey(String key);
  
  public Boolean getSearchResult();
  
  public void setSearchResult(Boolean searchResult);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public IExceptionModel getWarnings();
  
  public void setWarnings(IExceptionModel warnings);
  
  public String getType();
  
  public void setType(String type);
  
  public String getFileName();
  public void setFileName(String fileName);
}
