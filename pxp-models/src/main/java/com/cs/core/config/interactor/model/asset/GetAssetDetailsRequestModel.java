package com.cs.core.config.interactor.model.asset;

import java.util.Map;

public class GetAssetDetailsRequestModel implements IGetAssetDetailsRequestModel {
  
  private static final long          serialVersionUID = 1L;
  
  protected String                   container;
  
  protected String                   assetKey;
  
  protected IAssetServerDetailsModel assetServerDetails;
  
  protected String                   download;
  
  protected Map<String, String>      requestHeaders;
  
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
  public IAssetServerDetailsModel getAssetServerDetails()
  {
    if (this.assetServerDetails == null) {
      this.assetServerDetails = new AssetServerDetailsModel();
    }
    return this.assetServerDetails;
  }
  
  @Override
  public void setAssetServerDetails(IAssetServerDetailsModel assetServerDetails)
  {
    this.assetServerDetails = assetServerDetails;
  }
  
  @Override
  public String getDownload()
  {
    return download;
  }
  
  @Override
  public void setDownload(String download)
  {
    this.download = download;
  }
  
  @Override
  public Map<String, String> getRequestHeaders()
  {
    return requestHeaders;
  }
  
  @Override
  public void setRequestHeaders(Map<String, String> requestHeaders)
  {
    this.requestHeaders = requestHeaders;
  }
}
