package com.cs.core.config.interactor.model.export;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetResponseMapModel implements IGetResponseMapModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Map<String, Object> map              = new HashMap<>();
  
  @Override
  public Map<String, Object> getMap()
  {
    return map;
  }
  
  @JsonDeserialize(as = Map.class)
  @Override
  public void setMap(Map<String, Object> map)
  {
    this.map = map;
  }
}
