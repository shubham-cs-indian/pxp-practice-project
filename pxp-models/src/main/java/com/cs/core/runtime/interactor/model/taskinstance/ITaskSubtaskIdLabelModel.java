package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ITaskSubtaskIdLabelModel extends IModel {
  
  public static final String TASK_LABEL            = "taskLabel";
  public static final String SUBTASKS_ID_LABEL_MAP = "subtasksIdLabelMap";
  
  public String getTaskLabel();
  
  public void setTaskLabel(String taskLabel);
  
  public Map<String, String> getSubtasksIdLabelMap();
  
  public void setSubtasksIdLabelMap(Map<String, String> subtasksIdLabelMap);
}
