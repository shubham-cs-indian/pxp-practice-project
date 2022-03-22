package com.cs.di.config.interactor.model.configdetails;

import java.util.HashMap;
import java.util.Map;

public class GetConfigDataForTransformResponseModel implements IGetConfigDataForTransformResponseModel {

  private static final long serialVersionUID = 1L;
  
  Map<String, Object>       contextMap       = new HashMap<>();
  Map<String, Object>       relationshipMap  = new HashMap<>();
  
  @Override
  public Map<String, Object> getContextMap()
  {
    return contextMap;
  }
  
  @Override
  public void setContextMap(Map<String, Object> contextMap)
  {
    this.contextMap = contextMap;
  }
  
  @Override
  public Map<String, Object> getRelationshipMap()
  {
    return relationshipMap;
  }
  
  @Override
  public void setRelationshipMap(Map<String, Object> relationshipMap)
  {
    this.relationshipMap = relationshipMap;
  }
}
