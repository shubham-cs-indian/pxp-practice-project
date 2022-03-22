package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigEntityIdsCodeNamesResponseModel extends IModel {
  
  public static final String MAP = "map";
  
  public Map<String, String> getMap();
  
  public void setMap(Map<String, String> map);
}
