package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IEventModel extends IModel {
  
  public static String EVENT_LIST = "eventList";
  
  public List<IEventInformationModel> getEventList();
  
  public void setEventList(List<IEventInformationModel> eventList);
}
