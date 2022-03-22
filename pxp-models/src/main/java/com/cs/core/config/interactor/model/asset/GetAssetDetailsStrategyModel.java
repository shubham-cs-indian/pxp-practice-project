package com.cs.core.config.interactor.model.asset;

import java.io.InputStream;
import java.util.Map;

public class GetAssetDetailsStrategyModel implements IGetAssetDetailsStrategyModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Integer             responseCode;
  
  protected InputStream         inputStream;
  
  protected Map<String, String> responseHeaders;
  
  @Override
  public InputStream getInputStream()
  {
    return inputStream;
  }
  
  @Override
  public void setInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
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
  
  @Override
  public Map<String, String> getResponseHeaders()
  {
    return responseHeaders;
  }
  
  @Override
  public void setResponseHeaders(Map<String, String> responseHeaders)
  {
    this.responseHeaders = responseHeaders;
  }
}
