package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IAssetUploadDataModel extends IModel {
  
  public static final String STORAGE_URL    = "storageUrl";
  public static final String CONTAINER      = "container";
  public static final String ASSET_DATA_MAP = "assetDataMap";
  public static final String ASSET_KEY      = "assetKey";
  public static final String ASSET_BYTES    = "assetBytes";
  
  public String getStorageUrl();
  
  public void setStorageUrl(String storageUrl);
  
  public String getContainer();
  
  public void setContainer(String container);
  
  public Map<String, String> getAssetDataMap();
  
  public void setAssetDataMap(Map<String, String> assetDataMap);
  
  public String getAssetKey();
  
  public void setAssetKey(String assetKey);
  
  public byte[] getAssetBytes();
  
  public void setAssetBytes(byte[] assetBytes);
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
}
