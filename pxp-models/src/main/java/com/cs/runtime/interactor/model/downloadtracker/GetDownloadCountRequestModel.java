package com.cs.runtime.interactor.model.downloadtracker;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class GetDownloadCountRequestModel implements IGetDownloadCountRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          assetInstanceId;
  protected ITimeRange      timeRange        = new TimeRange();

  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
  
  @Override
  public ITimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @Override
  @JsonDeserialize(as = TimeRange.class)
  public void setTimeRange(ITimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
}
