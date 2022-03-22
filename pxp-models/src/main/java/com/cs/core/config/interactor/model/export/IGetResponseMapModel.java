package com.cs.core.config.interactor.model.export;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetResponseMapModel extends IModel {
  
  public static String MAP = "map";
  
  public Map<String, Object> getMap();
  
  public void setMap(Map<String, Object> map);
}
