package com.cs.core.runtime.interactor.model.camunda;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICamundaBroadcastEventModel extends IModel {
  
  public String getEventName();
  
  public void setEventName(String eventName);
  
  public Map<String, Object> getEventParameters();
  
  public void setEventParameters(Map<String, Object> eventParameters);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getEventId();
  
  public void setEventId(String eventId);
}
