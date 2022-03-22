package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.InputStream;
import java.util.Map;

public interface IGetAssetDetailsResponseModel extends IModel {
  
  public static final String INPUT_STREAM        = "inputStream";
  public static final String CONTENT_TYPE        = "contentType";
  public static final String CONTENT_DISPOSITION = "contentDisposition";
  public static final String RESPONSE_HEADERS    = "responseHeaders";
  
  public InputStream getInputStream();
  
  public void setInputStream(InputStream inputStream);
  
  public String getContentType();
  
  public void setContentType(String contentType);
  
  public String getContentDisposition();
  
  public void setContentDisposition(String contentDisposition);
  
  public Map<String, String> getResponseHeaders();
  
  public void setResponseHeaders(Map<String, String> responseHeaders);
}
