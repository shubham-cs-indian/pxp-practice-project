package com.cs.core.runtime.interactor.model.instancetree;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


public class GetInstanceTreeForCalendarRequestModel extends GetInstanceTreeRequestModel
    implements IGetInstanceTreeForCalendarRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected Set<String>     taskIdsHavingRP;
  protected Set<String>     personalTaskIds;
  protected String          currentUserId;
  protected String          roleIdOfCurrentUser;
  protected ITimeRange      selectedTimeRange;
  
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
  public String getCurrentUserId()
  {
    return currentUserId;
  }

  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
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

  @Override
  public ITimeRange getSelectedTimeRange()
  {
    return selectedTimeRange;
  }

  @Override
  @JsonDeserialize(as = TimeRange.class)
  public void setSelectedTimeRange(ITimeRange selectedTimeRange)
  {
    this.selectedTimeRange = selectedTimeRange;
  }
  
}
