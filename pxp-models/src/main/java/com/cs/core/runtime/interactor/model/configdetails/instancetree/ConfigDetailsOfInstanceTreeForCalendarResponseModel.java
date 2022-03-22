package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.HashSet;
import java.util.Set;


public class ConfigDetailsOfInstanceTreeForCalendarResponseModel extends ConfigDetailsForGetNewInstanceTreeModel
    implements IConfigDetailsOfInstanceTreeForCalendarResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     taskIdsHavingRP;
  protected Set<String>     personalTaskIds;
  protected String          roleIdOfCurrentUser;
  
  @Override
  public Set<String> getTaskIdsHavingRP()
  {
    if (taskIdsHavingRP == null) {
      taskIdsHavingRP = new HashSet<>();
    }
    return taskIdsHavingRP;
  }
  
  @Override
  public void setTaskIdsHavingRP(Set<String> taskIdsHavingRP)
  {
    this.taskIdsHavingRP = taskIdsHavingRP;
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    if (personalTaskIds == null) {
      personalTaskIds = new HashSet<>();
    }
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  @Override
  public String getRoleIdOfCurrentUser()
  {
    return roleIdOfCurrentUser;
  }

  @Override
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser)
  {
    this.roleIdOfCurrentUser = roleIdOfCurrentUser;
  }
}
