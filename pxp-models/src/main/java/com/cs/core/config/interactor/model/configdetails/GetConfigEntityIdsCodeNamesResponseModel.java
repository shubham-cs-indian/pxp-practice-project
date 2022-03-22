package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

public class GetConfigEntityIdsCodeNamesResponseModel
    implements IGetConfigEntityIdsCodeNamesResponseModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, String> map;
  
  @Override
  public Map<String, String> getMap()
  {
    return map;
  }
  
  @Override
  public void setMap(Map<String, String> map)
  {
    this.map = map;
  }
}
