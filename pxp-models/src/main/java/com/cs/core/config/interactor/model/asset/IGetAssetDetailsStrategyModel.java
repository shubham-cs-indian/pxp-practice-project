package com.cs.core.config.interactor.model.asset;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.InputStream;
import java.util.Map;

public interface IGetAssetDetailsStrategyModel extends IModel {
  
  public static final String RESPONSE_CODE    = "responseCode";
  public static final String INPUT_STREAM     = "inputStream";
  public static final String RESPONSE_HEADERS = "responseHeaders";
  
  public Integer getResponseCode();
  
  public void setResponseCode(Integer responseCode);
  
  public InputStream getInputStream();
  
  public void setInputStream(InputStream inputStream);
  
  public Map<String, String> getResponseHeaders();
  
  public void setResponseHeaders(Map<String, String> responseHeaders);
}
