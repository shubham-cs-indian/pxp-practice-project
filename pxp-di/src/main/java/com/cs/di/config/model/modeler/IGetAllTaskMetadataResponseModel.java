package com.cs.di.config.model.modeler;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.workflow.base.TaskType;

public interface IGetAllTaskMetadataResponseModel extends IModel {
  
  public static final String TASKS_MAP  = "tasksMap";
  
  public Map<TaskType, List<String>> getTasksMap();
  public void setTasksMap(Map<TaskType, List<String>> tasksMap);
}
