package com.cs.di.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetConfigDataForTransformResponseModel extends IModel {
  
  public static final String CONTEXT_MAP      = "contextMap";
  public static final String RELATIONSHIP_MAP = "relationshipMap";
  
  public Map<String, Object> getContextMap();
  public void setContextMap(Map<String, Object> contextMap);
  
  public Map<String, Object> getRelationshipMap();
  public void setRelationshipMap(Map<String, Object> relationshipMap);
}
