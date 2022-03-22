package com.cs.runtime.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface IGetDownloadCountRequestModel extends IModel {
  
  public static final String ASSET_INSTANCE_ID = "assetInstanceId";
  public static final String TIME_RANGE        = "timeRange";
  
  public String getAssetInstanceId();
  public void setAssetInstanceId(String assetInstanceId);
  
  public ITimeRange getTimeRange();
  public void setTimeRange(ITimeRange timeRange);
}
