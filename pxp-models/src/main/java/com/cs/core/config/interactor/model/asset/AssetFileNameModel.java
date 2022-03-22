package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetFileNameModel;

public class AssetFileNameModel implements IAssetFileNameModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          fileName;
  
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
