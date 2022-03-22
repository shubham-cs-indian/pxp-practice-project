package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.HashMap;
import java.util.Map;

public class TaskSubtaskIdLabelModel implements ITaskSubtaskIdLabelModel {
  
  private static final long     serialVersionUID   = 1L;
  
  protected String              taskLabel;
  protected Map<String, String> subtasksIdLabelMap = new HashMap<>();
  
  @Override
  public String getTaskLabel()
  {
    return taskLabel;
  }
  
  @Override
  public void setTaskLabel(String taskLabel)
  {
    this.taskLabel = taskLabel;
  }
  
  @Override
  public Map<String, String> getSubtasksIdLabelMap()
  {
    return subtasksIdLabelMap;
  }
  
  @Override
  public void setSubtasksIdLabelMap(Map<String, String> subtasksIdLabelMap)
  {
    this.subtasksIdLabelMap = subtasksIdLabelMap;
  }
}
