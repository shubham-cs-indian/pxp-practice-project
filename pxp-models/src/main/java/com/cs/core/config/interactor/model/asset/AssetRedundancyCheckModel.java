package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetRedundancyCheckModel;

public class AssetRedundancyCheckModel implements IAssetRedundancyCheckModel {
  
  private static final long serialVersionUID = 1L;
  protected String          hash;
  protected String          fileName;
  
  public AssetRedundancyCheckModel(String hash)
  {
    this.hash = hash;
  }
  
  public AssetRedundancyCheckModel(String hash, String fileName)
  {
    this.hash = hash;
    this.fileName = fileName;
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
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
}
