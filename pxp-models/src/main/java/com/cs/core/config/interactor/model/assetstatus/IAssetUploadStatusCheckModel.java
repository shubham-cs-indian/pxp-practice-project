package com.cs.core.config.interactor.model.assetstatus;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetUploadStatusCheckModel extends IModel {
  
  public static final String CONTAINER = "container";
  public static final String KEY       = "key";
  
  public String getContainer();
  
  public void setContainer(String container);
  
  public String getKey();
  
  public void setKey(String key);
}
