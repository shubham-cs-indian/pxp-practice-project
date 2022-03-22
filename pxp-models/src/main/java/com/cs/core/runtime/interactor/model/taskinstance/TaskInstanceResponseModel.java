package com.cs.core.runtime.interactor.model.taskinstance;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public class TaskInstanceResponseModel implements ITaskInstanceResponseModel {
  
  private static final long                    serialVersionUID = 1L;
  
  protected Map<String, IGetTaskInstanceModel> tasks            = new HashMap<>();
  
  @Override
  public Map<String, IGetTaskInstanceModel> getTasks()
  {
    return this.tasks;
  }
  
  @JsonDeserialize(contentAs = GetTaskInstanceModel.class)
  @Override
  public void setTasks(Map<String, IGetTaskInstanceModel> tasks)
  {
    this.tasks = tasks;
  }
}
