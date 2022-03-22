package com.cs.core.runtime.interactor.model.configuration;

public class EventInformationModel implements IEventInformationModel {
  
  protected String eventClass;
  protected String label;
  
  @Override
  public String getEventClass()
  {
    return eventClass;
  }
  
  @Override
  public void setEventClass(String eventName)
  {
    this.eventClass = eventName;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
}
