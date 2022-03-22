package com.cs.core.runtime.interactor.model.camunda;

import java.util.HashMap;
import java.util.Map;

public class CamundaBroadcastEventModel implements ICamundaBroadcastEventModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              eventName;
  protected Map<String, Object> eventParameters  = new HashMap<>();
  protected String              klassInstanceId;
  protected String              eventId;
  
  public CamundaBroadcastEventModel(String eventName)
  {
    this.eventName = eventName;
  }
  
  public CamundaBroadcastEventModel(String eventName, String klassInstanceId)
  {
    this.eventName = eventName;
    this.klassInstanceId = klassInstanceId;
  }
  
  public CamundaBroadcastEventModel(String eventName, String klassInstanceId,
      Map<String, Object> eventParameters)
  {
    this.eventName = eventName;
    this.klassInstanceId = klassInstanceId;
    this.eventParameters = eventParameters;
  }
  
  public CamundaBroadcastEventModel(String eventName, Map<String, Object> eventParameters)
  {
    this.eventName = eventName;
    this.eventParameters = eventParameters;
  }
  
  @Override
  public String getEventName()
  {
    return eventName;
  }
  
  @Override
  public void setEventName(String eventName)
  {
    this.eventName = eventName;
  }
  
  @Override
  public Map<String, Object> getEventParameters()
  {
    return eventParameters;
  }
  
  @Override
  public void setEventParameters(Map<String, Object> eventParameters)
  {
    this.eventParameters = eventParameters;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getEventId()
  {
    return eventId;
  }
  
  @Override
  public void setEventId(String eventId)
  {
    this.eventId = eventId;
  }
}
