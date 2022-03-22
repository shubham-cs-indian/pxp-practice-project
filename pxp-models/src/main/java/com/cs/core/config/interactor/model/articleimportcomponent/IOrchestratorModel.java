package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IOrchestratorModel extends IModel {
  
  public Boolean getComponentStatus();
  
  public void setComponentStatus(Boolean componentStatus);
  
  public Boolean getFailedStatus();
  
  public void setFailedStatus(Boolean failedStatus);
  
  public Map<String, Object> getComponentObject();
  
  public void setComponentObject(Map<String, Object> componentObject);
  
  public String getId();
  
  public void setId(String id);
  
  public String getComponentId();
  
  public void setComponentId(String componentId);
}
