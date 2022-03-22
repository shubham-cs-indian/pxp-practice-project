package com.cs.core.config.interactor.entity.action;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IAction extends IEntity {
  
  public IParameters getParameters();
  
  public void setParameters(IParameters parameters);
  
  public String getTaskId();
  
  public void setTaskId(String taskId);
}
