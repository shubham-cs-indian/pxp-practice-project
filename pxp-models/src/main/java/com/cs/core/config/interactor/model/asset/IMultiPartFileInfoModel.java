package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IMultiPartFileInfoModel extends IIdsListParameterModel {
  
  public static final String KEY               = "key";
  public static final String ORIGINAL_FILENAME = "originalFilename";
  public static final String BYTES             = "bytes";
  
  public String getKey();
  
  public void setKey(String key);
  
  public byte[] getBytes();
  
  public void setBytes(byte[] bytes);
  
  public String getOriginalFilename();
  
  public void setOriginalFilename(String originalFilename);
}
