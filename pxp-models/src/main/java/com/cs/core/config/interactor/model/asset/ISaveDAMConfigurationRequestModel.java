package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface ISaveDAMConfigurationRequestModel extends IModel {
  
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  
  public boolean isShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);

}
