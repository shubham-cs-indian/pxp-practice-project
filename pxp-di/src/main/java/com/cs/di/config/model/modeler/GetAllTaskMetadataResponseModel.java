package com.cs.di.config.model.modeler;

import java.util.List;
import java.util.Map;

import com.cs.workflow.base.TaskType;

public class GetAllTaskMetadataResponseModel implements IGetAllTaskMetadataResponseModel {  

  private static final long           serialVersionUID = 1L;
  private Map<TaskType, List<String>> tasksMap;
  
  public Map<TaskType, List<String>> getTasksMap()
  {
    return tasksMap;
  }
  
  public void setTasksMap(Map<TaskType, List<String>> tasksMap)
  {
    this.tasksMap = tasksMap;
  }
}
