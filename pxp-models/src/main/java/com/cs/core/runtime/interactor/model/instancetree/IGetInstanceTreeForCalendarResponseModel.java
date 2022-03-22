package com.cs.core.runtime.interactor.model.instancetree;

import java.util.Map;

import com.cs.core.runtime.interactor.model.eventinstance.IEventInstanceListModel;
import com.cs.core.runtime.interactor.model.taskinstance.ITaskInstanceListModel;

public interface IGetInstanceTreeForCalendarResponseModel
    extends IGetNewInstanceTreeResponseModel {
  
  public static final String TASKS  = "tasks";
  public static final String EVENTS = "events";
  
  public Map<String, ITaskInstanceListModel> getTasks();
  public void setTasks(Map<String, ITaskInstanceListModel> tasks);
  
  public Map<String, IEventInstanceListModel> getEvents();
  public void setEvents(Map<String, IEventInstanceListModel> events);
}
