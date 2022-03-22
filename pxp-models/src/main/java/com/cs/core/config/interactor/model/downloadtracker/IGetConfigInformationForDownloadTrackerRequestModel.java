package com.cs.core.config.interactor.model.downloadtracker;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigInformationForDownloadTrackerRequestModel extends IModel {
  
  public static final String ASSET_CLASS_IDS = "assetClassIds";
  public static final String USER_IDS        = "userIds";
  
  public List<String> getAssetClassIds();
  public void setAssetClassIds(List<String> assetClassIds);
  
  public List<String> getUserIds();
  public void setUserIds(List<String> userIds);
  
}
