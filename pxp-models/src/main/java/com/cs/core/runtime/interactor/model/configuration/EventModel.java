package com.cs.core.runtime.interactor.model.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class EventModel implements IEventModel {
  
  protected List<IEventInformationModel> eventList;
  
  @Override
  public List<IEventInformationModel> getEventList()
  {
    if (eventList == null) {
      eventList = new ArrayList<>();
    }
    return eventList;
  }
  
  @JsonDeserialize(contentAs = EventInformationModel.class)
  @Override
  public void setEventList(List<IEventInformationModel> eventList)
  {
    this.eventList = eventList;
  }
}
