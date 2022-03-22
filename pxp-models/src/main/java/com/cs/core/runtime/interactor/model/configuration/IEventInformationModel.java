package com.cs.core.runtime.interactor.model.configuration;

public interface IEventInformationModel extends IModel {
  
  public static String EVENT_CLASS = "eventClass";
  public static String LABEL       = "label";
  
  public String getEventClass();
  
  public void setEventClass(String eventName);
  
  public String getLabel();
  
  public void setLabel(String label);
}
