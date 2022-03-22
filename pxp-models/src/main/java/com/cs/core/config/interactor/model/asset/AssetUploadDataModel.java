package com.cs.core.config.interactor.model.asset;

import java.util.Map;

public class AssetUploadDataModel implements IAssetUploadDataModel {
  
  protected String              storageUrl;
  protected String              container;
  protected Map<String, String> assetDataMap;
  protected String              assetKey;
  protected byte[]              assetBytes;
  protected String              assetInstanceId;
  
  @Override
  public String getStorageUrl()
  {
    return storageUrl;
  }
  
  @Override
  public void setStorageUrl(String storageUrl)
  {
    this.storageUrl = storageUrl;
  }
  
  @Override
  public String getContainer()
  {
    return container;
  }
  
  @Override
  public void setContainer(String container)
  {
    this.container = container;
  }
  
  @Override
  public Map<String, String> getAssetDataMap()
  {
    return assetDataMap;
  }
  
  @Override
  public void setAssetDataMap(Map<String, String> assetDataMap)
  {
    this.assetDataMap = assetDataMap;
  }
  
  @Override
  public String getAssetKey()
  {
    return assetKey;
  }
  
  @Override
  public void setAssetKey(String assetKey)
  {
    this.assetKey = assetKey;
  }
  
  @Override
  public byte[] getAssetBytes()
  {
    return assetBytes;
  }
  
  @Override
  public void setAssetBytes(byte[] assetBytes)
  {
    this.assetBytes = assetBytes;
  }
  
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
}
