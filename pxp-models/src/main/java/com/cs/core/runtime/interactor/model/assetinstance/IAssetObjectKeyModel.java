package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetObjectKeyModel extends IModel {
  
  public static final String ASSET_OBJECT_KEY = "assetObjectKey";
  public static final String MODE             = "mode";
  
  public String getAssetObjectKey();
  
  public void setAssetObjectKey(String assetObjectKey);
  
  public String getMode();
  
  public void setMode(String mode);
}
