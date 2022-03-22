package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetAssetDetailsRequestModel extends IModel {
  
  public static final String CONTAINER            = "container";
  public static final String ASSET_KEY            = "assetKey";
  public static final String ASSET_SERVER_DETAILS = "assetServerDetails";
  public static final String DOWNLOAD             = "download";
  public static final String REQUEST_HEADERS      = "requestHeaders";
  
  public String getContainer();
  
  public void setContainer(String container);
  
  public String getAssetKey();
  
  public void setAssetKey(String assetKey);
  
  public IAssetServerDetailsModel getAssetServerDetails();
  
  public void setAssetServerDetails(IAssetServerDetailsModel assetServerDetails);
  
  public String getDownload();
  
  public void setDownload(String download);
  
  public Map<String, String> getRequestHeaders();
  
  public void setRequestHeaders(Map<String, String> requestHeaders);
}
