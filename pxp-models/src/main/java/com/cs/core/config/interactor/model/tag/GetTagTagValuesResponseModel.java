package com.cs.core.config.interactor.model.tag;

import java.util.List;
import java.util.Map;

public class GetTagTagValuesResponseModel implements IGetTagTagValuesResponseModel {
  
  private static final long           serialVersionUID = 1L;
  protected Map<String, List<String>> map;
  
  @Override
  public Map<String, List<String>> getMap()
  {
    return map;
  }
  
  @Override
  public void setMap(Map<String, List<String>> map)
  {
    this.map = map;
  }
}
