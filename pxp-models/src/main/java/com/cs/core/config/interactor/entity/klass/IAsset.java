package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;

import java.util.List;

public interface IAsset extends IKlass {
  
  public static final String DETECT_DUPLICATE                             = "detectDuplicate";
  public static final String UPLOAD_ZIP                                   = "uploadZip";
  public static final String EXTENSION_CONFIGURATION                      = "extensionConfiguration";
  public static final String INDESIGN_SERVER                              = "indesignServer";
  public static final String TRACK_DOWNLOADS                              = "trackDownloads";
  public static final String IS_DETECTDUPLICATE_MODIFIED                  = "isDetectDuplicateModified";
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  
  public Boolean getDetectDuplicate();
  public void setDetectDuplicate(Boolean detectDuplicate);
  
  public Boolean getUploadZip();
  public void setUploadZip(Boolean uploadZip);
  
  public List<IAssetExtensionConfiguration> getExtensionConfiguration();
  public void setExtensionConfiguration(List<IAssetExtensionConfiguration> extensionConfiguration);
  
  public Boolean getIndesignServer();
  public void setIndesignServer(Boolean indesignServer);
  
  public boolean getTrackDownloads();
  public void setTrackDownloads(boolean trackDownloads);
  
  public Boolean getIsDetectDuplicateModified();
  public void setIsDetectDuplicateModified(Boolean isDetectDuplicateModified);
  
  public boolean isShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);
}
