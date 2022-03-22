package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;

public class AssetUploadModel implements IAssetUploadModel {
  
  private static final long       serialVersionUID = 1L;
  
  protected IAssetUploadDataModel assetUploadDataModel;
  protected String                assetSourcePath;
  
  @Override
  public IAssetUploadDataModel getAssetUploadDataModel()
  {
    return assetUploadDataModel;
  }
  
  @Override
  public void setAssetUploadDataModel(IAssetUploadDataModel assetUploadDataModel)
  {
    this.assetUploadDataModel = assetUploadDataModel;
  }
  
  @Override
  public String getAssetSourcePath()
  {
    
    return assetSourcePath;
  }
  
  @Override
  public void setAssetSourcePath(String assetSourcePath)
  {
    this.assetSourcePath = assetSourcePath;
  }
}
