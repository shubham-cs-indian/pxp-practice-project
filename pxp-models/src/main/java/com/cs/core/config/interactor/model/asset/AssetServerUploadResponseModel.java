package com.cs.core.config.interactor.model.asset;

public class AssetServerUploadResponseModel implements IAssetServerUploadResponseModel {
  
  protected Integer responseCode;
  
  public AssetServerUploadResponseModel(Integer responseCode)
  {
    this.responseCode = responseCode;
  }
  
  @Override
  public Integer getResponseCode()
  {
    return responseCode;
  }
  
  @Override
  public void setResponseCode(Integer responseCode)
  {
    this.responseCode = responseCode;
  }
}
