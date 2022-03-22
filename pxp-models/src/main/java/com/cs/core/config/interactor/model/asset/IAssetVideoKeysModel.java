package com.cs.core.config.interactor.model.asset;

public interface IAssetVideoKeysModel extends IAssetKeysModel {
  
  public static final String MP4_KEY = "mp4Key";
  
  public String getMp4Key();
  
  public void setMp4Key(String mp4Key);
}
