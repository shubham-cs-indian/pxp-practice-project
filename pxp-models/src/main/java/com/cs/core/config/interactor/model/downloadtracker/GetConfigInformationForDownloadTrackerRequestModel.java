package com.cs.core.config.interactor.model.downloadtracker;

import java.util.ArrayList;
import java.util.List;

public class GetConfigInformationForDownloadTrackerRequestModel
    implements IGetConfigInformationForDownloadTrackerRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    assetClassIds    = new ArrayList<>();
  protected List<String>    userIds          = new ArrayList<>();
  
  @Override
  public List<String> getUserIds()
  {
    return userIds;
  }
  
  @Override
  public void setUserIds(List<String> userIds)
  {
    this.userIds = userIds;
  }
  
  @Override
  public List<String> getAssetClassIds()
  {
    return assetClassIds;
  }
  
  @Override
  public void setAssetClassIds(List<String> assetClassIds)
  {
    this.assetClassIds = assetClassIds;
  }
  
}
