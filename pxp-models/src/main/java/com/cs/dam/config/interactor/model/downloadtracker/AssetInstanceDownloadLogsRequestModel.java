package com.cs.dam.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AssetInstanceDownloadLogsRequestModel implements IAssetInstanceDownloadLogsRequestModel {
  
  private static final long                             serialVersionUID                      = 1L;
  protected List<IAssetDownloadInformationWithTIVModel> assetDownloadInformationWithTIVModels = new ArrayList<>();
  protected String                                      comments;
  protected String                                      downloadId;
  protected Set<String>                                 klassIdList                           = new HashSet<>();
  protected List<Long>                                  assetInstanceIdListGettingDownloaded  = new ArrayList<>();
  
  @Override
  public List<IAssetDownloadInformationWithTIVModel> getAssetDownloadInformationWithTIVModels()
  {
    return assetDownloadInformationWithTIVModels;
  }

  @JsonDeserialize(contentAs = AssetDownloadInformationWithTIVModel.class)
  @Override
  public void setAssetDownloadInformationWithTIVModels(List<IAssetDownloadInformationWithTIVModel> assetDownloadInformationWithTIVModels)
  {
    this.assetDownloadInformationWithTIVModels = assetDownloadInformationWithTIVModels;
  }

  @Override
  public String getComments()
  {
    return comments;
  }
  
  @Override
  public void setComments(String comments)
  {
    this.comments = comments;
  }
  
  @Override
  public String getDownloadId()
  {
    return downloadId;
  }
  
  @Override
  public void setDownloadId(String downloadId)
  {
    this.downloadId = downloadId;
  }

  @Override
  public Set<String> getKlassIdList()
  {
    return klassIdList;
  }

  @Override
  public void setKlassIdList(Set<String> klassIdList)
  {
    this.klassIdList = klassIdList;
  }

  @Override
  public List<Long> getAssetInstanceIdListGettingDownloaded()
  {
    return assetInstanceIdListGettingDownloaded;
  }

  @Override
  public void setAssetInstanceIdListGettingDownloaded(List<Long> assetInstanceIdListGettingDownloaded)
  {
    this.assetInstanceIdListGettingDownloaded = assetInstanceIdListGettingDownloaded;
  }
}
