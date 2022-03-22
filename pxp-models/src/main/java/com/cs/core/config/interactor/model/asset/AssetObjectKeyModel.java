package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetObjectKeyModel;

public class AssetObjectKeyModel implements IAssetObjectKeyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          assetObjectKey;
  protected String          mode;
  
  public AssetObjectKeyModel(String assetObjectKey, String mode)
  {
    this.assetObjectKey = assetObjectKey;
    this.mode = mode;
  }
  
  @Override
  public String getAssetObjectKey()
  {
    return assetObjectKey;
  }
  
  @Override
  public void setAssetObjectKey(String assetObjectKey)
  {
    this.assetObjectKey = assetObjectKey;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }
}
