package com.cs.core.config.interactor.model.asset;

public class SaveDAMConfigurationRequestModel implements ISaveDAMConfigurationRequestModel {

  private static final long serialVersionUID                        = 1L;
  protected boolean         shouldDownloadAssetWithOriginalFilename = false;
  
  @Override
  public boolean isShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public void setShouldDownloadAssetWithOriginalFilename(
      boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
  
  
  
}
