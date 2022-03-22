package com.cs.core.config.interactor.model.articleimportcomponent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class OrchestratorInstanceModel implements IOrchestratorInstanceModel {
  
  private static final long       serialVersionUID = 1L;
  
  Map<String, IOrchestratorModel> events;
  Boolean                         runningStatus    = true;
  
  @Override
  public Map<String, IOrchestratorModel> getEvents()
  {
    if (events == null) {
      events = new HashMap<>();
    }
    return events;
  }
  
  @Override
  @JsonDeserialize(contentAs = OrchestratorModel.class)
  public void setEvents(Map<String, IOrchestratorModel> events)
  {
    this.events = events;
  }
  
  @Override
  public Boolean getRunningStatus()
  {
    return runningStatus;
  }
  
  @Override
  public void setRunningStatus(Boolean running)
  {
    this.runningStatus = running;
  }
}
