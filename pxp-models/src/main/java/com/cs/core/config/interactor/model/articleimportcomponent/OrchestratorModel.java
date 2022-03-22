package com.cs.core.config.interactor.model.articleimportcomponent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class OrchestratorModel implements IOrchestratorModel {
  
  private static final long serialVersionUID = 1L;
  
  Boolean                   completionStatus = false;
  Boolean                   failureStatus    = false;
  Map<String, Object>       componentObject  = new HashMap<>();
  String                    id;
  String                    componentId;
  
  @Override
  public Boolean getComponentStatus()
  {
    return completionStatus;
  }
  
  @Override
  public void setComponentStatus(Boolean componentStatus)
  {
    this.completionStatus = componentStatus;
  }
  
  @Override
  public Boolean getFailedStatus()
  {
    return failureStatus;
  }
  
  @Override
  public void setFailedStatus(Boolean failedStatus)
  {
    this.failureStatus = failedStatus;
  }
  
  @Override
  @JsonDeserialize(as = Map.class)
  public Map<String, Object> getComponentObject()
  {
    if (componentObject == null) {
      componentObject = new HashMap<>();
    }
    return componentObject;
  }
  
  @Override
  public void setComponentObject(Map<String, Object> componentObject)
  {
    this.componentObject = componentObject;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getComponentId()
  {
    return componentId;
  }
  
  @Override
  public void setComponentId(String componentId)
  {
    this.componentId = componentId;
  }
}
