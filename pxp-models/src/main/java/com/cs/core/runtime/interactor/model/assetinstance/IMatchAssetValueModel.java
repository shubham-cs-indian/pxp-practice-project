package com.cs.core.runtime.interactor.model.assetinstance;

public interface IMatchAssetValueModel {
  
  public static final String ASSET_INSTANCE_ID = "assetInstanceId";
  public static final String PERCENT           = "percent";
  public static final String COUNT             = "count";
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getPercent();
  
  public void setPercent(String percentage);
  
  public String getCount();
  
  public void setCount(String count);
}
