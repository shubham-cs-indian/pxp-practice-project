package com.cs.runtime.interactor.model.indsserver;

public class INDSTaskRequestModel implements IINDSTaskRequestModel {
  
  private static final long serialVersionUID = 1L;
  private String            taskId = "";
  
  @Override
  public String getTaskId()
  {
    return taskId;
  }
  
  @Override
  public void setTaskId(String taskId)
  {
    this.taskId = taskId;
  }
  
}