package com.cs.core.config.interactor.model.asset;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;

public class DAMConfigurationDetailsResponseModel extends ConfigResponseWithAuditLogModel implements IDAMConfigurationDetailsResponseModel {
  
  private static final long serialVersionUID                        = 1L;
  protected boolean         shouldDownloadAssetWithOriginalFilename = false;
  
  @Override
  public boolean getShouldDownloadAssetWithOriginalFilename()
  {
    return shouldDownloadAssetWithOriginalFilename;
  }
  
  @Override
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename)
  {
    this.shouldDownloadAssetWithOriginalFilename = shouldDownloadAssetWithOriginalFilename;
  }
}
