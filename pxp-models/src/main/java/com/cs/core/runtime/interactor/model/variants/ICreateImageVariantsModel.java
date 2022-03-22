package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;

public interface ICreateImageVariantsModel extends ICreateVariantModel {
  
  public static final String ASSET_INFORMATION = "assetInformation";
  public static final String THUMBNAIL_PATH    = "thumbnailPath";
  
  public IAssetInformationModel getAssetInformation();
  public void setAssetInformation(IAssetInformationModel assetInformation);
  
  public String getThumbnailPath();
  public void setThumbnailPath(String thumbnailPath);
}
