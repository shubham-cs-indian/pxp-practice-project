package com.cs.core.config.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetTagTagValuesResponseModel extends IModel {
  
  public static final String MAP = "map";
  
  public Map<String, List<String>> getMap();
  
  public void setMap(Map<String, List<String>> map);
}
