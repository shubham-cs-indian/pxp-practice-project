package com.cs.core.config.interactor.model.asset;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;


public interface IDAMConfigurationDetailsResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  
  public boolean getShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);
  
}
