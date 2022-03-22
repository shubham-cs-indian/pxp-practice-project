package com.cs.runtime.interactor.model.indsserver;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IINDSTaskRequestModel extends IModel {
  
  public final static String TASK_ID = "taskId";
  
  public String getTaskId();
  public void setTaskId(String taskId);
  
}