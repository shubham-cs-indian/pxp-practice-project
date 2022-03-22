package com.cs.runtime.interactor.model.indsserver;

import java.io.InputStream;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IScriptResponseModel extends IModel {
  
  public final static String RESPONSE_MAP = "responseMap";
  public final static String INPUT_STREAM = "inputStream";
  
  public Map<String, Object> getResponseMap();
  public void setResponseMap(Map<String, Object> responseMap);
  
  public InputStream getInputStream();
  public void setInputStream(InputStream inputStream);
  
}