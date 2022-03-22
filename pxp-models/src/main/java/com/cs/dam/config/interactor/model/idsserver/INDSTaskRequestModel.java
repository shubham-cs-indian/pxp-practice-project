package com.cs.dam.config.interactor.model.idsserver;

public class INDSTaskRequestModel implements IINDSTaskRequestModel {
  
  private static final long serialVersionUID = 1L;
  private String            taskId           = "";
  
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
