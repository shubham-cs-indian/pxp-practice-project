package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkAssetDownloadWithVariantsModel extends IModel {
  
  public static final String DOWNLOAD_INFORMATION                         = "downloadInformation";
  public static final String SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME = "shouldDownloadAssetWithOriginalFilename";
  public static final String DOWNLOAD_FILE_NAME                           = "downloadFileName";
  public static final String COMMENTS                                     = "comments";
  public static final String SHOULD_CREATE_SEPARATE_FOLDER                = "shouldCreateSeparateFolders";
  public static final String IS_DOWNLOADED_FROM_INSTANCE                  = "isDownloadedFromInstance";
  public static final String DOWNLOAD_WITHIN_TIME_RANGE                   = "downloadWithinTimeRange";
  public static final String SHOULD_TRACK_DOWNLOADS                       = "shouldTrackDownloads";
  
  public List<IAssetDownloadInformationWithTIVModel> getDownloadInformation();
  public void setDownloadInformation(List<IAssetDownloadInformationWithTIVModel> downloadInformation);
  
  public boolean getShouldDownloadAssetWithOriginalFilename();
  public void setShouldDownloadAssetWithOriginalFilename(boolean shouldDownloadAssetWithOriginalFilename);
  
  public String getDownloadFileName();  
  public void setDownloadFileName(String downloadFileName);
  
  public String getComments();  
  public void setComments(String comments);
  
  public boolean getShouldCreateSeparateFolders();
  public void setShouldCreateSeparateFolders(boolean shouldCreateSeparateFolders);
  
  public boolean getIsDownloadedFromInstance();
  public void setDownloadedFromInstance(boolean isDownloadedFromInstance);
  
  public ITimeRange getDownloadWithinTimeRange();
  public void setDownloadWithinTimeRange(ITimeRange downloadWithinTimeRange);
  
  public boolean getShouldTrackDownloads();
  public void setShouldTrackDownloads(boolean shouldTrackDownloads);
  
}
