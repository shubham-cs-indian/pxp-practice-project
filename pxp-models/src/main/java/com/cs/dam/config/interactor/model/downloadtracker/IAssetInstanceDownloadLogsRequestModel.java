package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetInstanceDownloadLogsRequestModel extends IModel {
  
  public static final String ASSET_DOWNLOAD_INFORMATION_WITH_TIV_MODELS = "assetDownloadInformationWithTIVModels";
  public static final String COMMENTS                                   = "comments";
  public static final String DOWNLOAD_ID                                = "downloadId";
  public static final String KLASS_ID_LIST                              = "klassIdList";
  public static final String ASSET_INSTANCE_ID_LIST_GETTING_DOWNLOADED  = "assetInstanceIdListGettingDownloaded";
  
  public String getComments();
  public void setComments(String comments);
  
  public String getDownloadId();
  public void setDownloadId(String downloadId);
  
  public List<IAssetDownloadInformationWithTIVModel> getAssetDownloadInformationWithTIVModels();
  public void setAssetDownloadInformationWithTIVModels(List<IAssetDownloadInformationWithTIVModel> assetDownloadInformationWithTIVModels);
  
  public Set<String> getKlassIdList();
  public void setKlassIdList(Set<String> klassIdList);
  
  public List<Long> getAssetInstanceIdListGettingDownloaded();
  public void setAssetInstanceIdListGettingDownloaded(List<Long> assetInstanceIdListGettingDownloaded);
}
