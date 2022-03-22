package com.cs.core.runtime.interactor.model.assetinstance;

public class MatchAssetValueModel implements IMatchAssetValueModel {
  
  protected String assetInstanceId;
  protected String percent;
  protected String count;
  
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
  public String getPercent()
  {
    return percent;
  }
  
  @Override
  public void setPercent(String percentage)
  {
    this.percent = percentage;
  }
  
  @Override
  public String getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(String count)
  {
    this.count = count;
  }
}
