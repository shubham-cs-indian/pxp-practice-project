package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface ICreateTaskInstanceConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_ROLES = "referencedRoles";
  public static final String REFERENCED_TASK  = "referencedTask";
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public ITaskModel getReferencedTask();
  
  public void setReferencedTask(ITaskModel referencedTask);
}
