package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetUploadDataKeysModel extends IModel {
  
  public static final String KEY          = "key";
  public static final String NAME         = "name";
  public static final String FORMAT       = "format";
  public static final String TYPE         = "type";
  public static final String BYTES        = "bytes";
  public static final String CONTENT_TYPE = "contentType";
  
  public String getKey();
  
  public void setKey(String key);
  
  public String getName();
  
  public void setName(String name);
  
  public String getFormat();
  
  public void setFormat(String format);
  
  public String getType();
  
  public void setType(String type);
  
  public byte[] getBytes();
  
  public void setBytes(byte[] bytes);
  
  public String getContentType();
  
  public void setContentType(String contentType);
}
