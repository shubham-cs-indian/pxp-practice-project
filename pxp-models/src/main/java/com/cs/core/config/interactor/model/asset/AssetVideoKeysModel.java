package com.cs.core.config.interactor.model.asset;

import com.cs.core.config.interactor.model.configdetails.AbstractAssetKeysModel;

import java.util.Map;

public class AssetVideoKeysModel extends AbstractAssetKeysModel implements IAssetVideoKeysModel {
  
  protected String mp4Key;
  
  public AssetVideoKeysModel(String imageKey, String thumbKey, Map<String, Object> metadata,
      String hash, String mp4Key, String key, String name, String klassId)
  {
    super(imageKey, thumbKey, metadata, hash, key, name, klassId);
    this.mp4Key = mp4Key;
  }
  
  @Override
  public String getMp4Key()
  {
    return mp4Key;
  }
  
  @Override
  public void setMp4Key(String mp4Key)
  {
    this.mp4Key = mp4Key;
  }
}
