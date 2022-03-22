package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IOrchestratorInstanceModel extends IModel {
  
  public Map<String, IOrchestratorModel> getEvents();
  
  public void setEvents(Map<String, IOrchestratorModel> events);
  
  public Boolean getRunningStatus();
  
  public void setRunningStatus(Boolean running);
}
