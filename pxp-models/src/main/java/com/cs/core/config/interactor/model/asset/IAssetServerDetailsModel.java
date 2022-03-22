package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAssetServerDetailsModel extends IModel {
  
  public static final String STORAGE_URL = "storageURL";
  public static final String AUTH_TOKEN  = "authToken";
  
  public String getStorageURL();
  
  public void setStorageURL(String storageURL);
  
  public String getAuthToken();
  
  public void setAuthToken(String authToken);
}
