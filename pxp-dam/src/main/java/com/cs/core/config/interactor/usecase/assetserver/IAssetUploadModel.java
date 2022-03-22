package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetUploadModel extends IModel {
  
  public static final String ASSET_UPLOAD_DATAMODEL = "assetUploadDataModel";
  public static final String ASSET_SOURCE_PATH      = "assetSourcePath";
  
  public IAssetUploadDataModel getAssetUploadDataModel();
  
  public void setAssetUploadDataModel(IAssetUploadDataModel assetUploadDataModel);
  
  public String getAssetSourcePath();
  
  public void setAssetSourcePath(String assetSourcePath);
}
