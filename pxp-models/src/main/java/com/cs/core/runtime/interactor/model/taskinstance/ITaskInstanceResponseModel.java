package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ITaskInstanceResponseModel extends IModel {
  
  public static final String TASKS = "tasks";
  
  public Map<String, IGetTaskInstanceModel> getTasks();
  
  public void setTasks(Map<String, IGetTaskInstanceModel> tasks);
}
