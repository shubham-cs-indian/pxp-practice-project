package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class CreateTaskInstanceConfigDetailsModel implements ICreateTaskInstanceConfigDetailsModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected Map<String, IRole> referencedRoles;
  protected ITaskModel         referencedTask;
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public ITaskModel getReferencedTask()
  {
    return referencedTask;
  }
  
  @JsonDeserialize(as = TaskModel.class)
  @Override
  public void setReferencedTask(ITaskModel referencedTask)
  {
    this.referencedTask = referencedTask;
  }
}
