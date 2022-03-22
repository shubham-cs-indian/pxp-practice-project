package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetRedundancyCheckModel extends IModel {
  
  public static final String HASH      = "hash";
  public static final String FILE_NAME = "fileName";
  
  public String getHash();
  
  public void setHash(String hash);
  
  public String getFileName();
  
  public void setFileName(String fileName);
}
