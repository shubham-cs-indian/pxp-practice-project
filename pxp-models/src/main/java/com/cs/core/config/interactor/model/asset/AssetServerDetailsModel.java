package com.cs.core.config.interactor.model.asset;

public class AssetServerDetailsModel implements IAssetServerDetailsModel {
  
  protected String storageURL;
  protected String authToken;
  
  public AssetServerDetailsModel()
  {
  }
  
  public AssetServerDetailsModel(String storageURL, String authToken)
  {
    this.storageURL = storageURL;
    this.authToken = authToken;
  }
  
  public AssetServerDetailsModel(IAssetServerDetailsModel model)
  {
    this.storageURL = model.getStorageURL();
    this.authToken = model.getAuthToken();
  }
  
  @Override
  public String getStorageURL()
  {
    return storageURL;
  }
  
  @Override
  public void setStorageURL(String storageURL)
  {
    this.storageURL = storageURL;
  }
  
  @Override
  public String getAuthToken()
  {
    return authToken;
  }
  
  @Override
  public void setAuthToken(String authToken)
  {
    this.authToken = authToken;
  }
}
