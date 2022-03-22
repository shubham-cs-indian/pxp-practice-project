package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;

import java.io.InputStream;
import java.util.Map;

public class GetAssetDetailsResponseModel implements IGetAssetDetailsResponseModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected InputStream         inputStream;
  
  protected String              contentType;
  
  protected String              contentDisposition;
  
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
  public String getContentType()
  {
    return contentType;
  }
  
  @Override
  public void setContentType(String contentType)
  {
    this.contentType = contentType;
  }
  
  @Override
  public String getContentDisposition()
  {
    return contentDisposition;
  }
  
  @Override
  public void setContentDisposition(String contentDisposition)
  {
    this.contentDisposition = contentDisposition;
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
