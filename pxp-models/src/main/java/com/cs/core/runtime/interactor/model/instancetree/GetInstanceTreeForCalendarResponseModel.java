package com.cs.core.runtime.interactor.model.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.runtime.interactor.model.eventinstance.EventInstanceListModel;
import com.cs.core.runtime.interactor.model.eventinstance.IEventInstanceListModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceListModel;
import com.cs.core.runtime.interactor.model.taskinstance.TaskInstanceListModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstanceTreeForCalendarResponseModel extends GetNewInstanceTreeResponseModel
    implements IGetInstanceTreeForCalendarResponseModel {
  
  private static final long                      serialVersionUID = 1L;
  protected Map<String, ITaskInstanceListModel>  tasks;
  protected Map<String, IEventInstanceListModel> events;
  
  @Override
  public Map<String, ITaskInstanceListModel> getTasks()
  {
    if(tasks == null) {
      tasks = new HashMap<>();
    }
    return tasks;
  }
  
  @Override
  @JsonDeserialize(contentAs = TaskInstanceListModel.class)
  public void setTasks(Map<String, ITaskInstanceListModel> tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public Map<String, IEventInstanceListModel> getEvents()
  {
    if(events == null) {
      events = new HashMap<>();
    }
    return events;
  }
  
  @Override
  @JsonDeserialize(contentAs = EventInstanceListModel.class)
  public void setEvents(Map<String, IEventInstanceListModel> events)
  {
    this.events = events;
  }
}