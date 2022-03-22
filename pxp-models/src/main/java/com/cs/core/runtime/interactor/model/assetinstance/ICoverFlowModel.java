package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICoverFlowModel extends IModel {
  
  public static final String ASSET_OBJECT_KEY = "assetObjectKey";
  public static final String FILE_NAME        = "fileName";
  public static final String TYPE             = "type";
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String assetObjectKey);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String getType();
  
  public void setType(String type);
}
