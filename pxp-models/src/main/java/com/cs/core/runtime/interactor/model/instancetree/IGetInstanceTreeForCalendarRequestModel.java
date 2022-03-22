package com.cs.core.runtime.interactor.model.instancetree;

import java.util.Set;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;

public interface IGetInstanceTreeForCalendarRequestModel extends IGetInstanceTreeRequestModel {
  
  public static final String TASK_IDS_HAVING_RP      = "taskIdsHavingRP";
  public static final String PERSONAL_TASK_IDS       = "personalTaskIds";
  public static final String CURRENT_USER_ID         = "currentUserId";
  public static final String ROLE_ID_OF_CURRENT_USER = "roleIdOfCurrentUser";
  public static final String SELECTED_TIME_RANGE     = "selectedTimeRange";
  
  public Set<String> getTaskIdsHavingRP();
  public void setTaskIdsHavingRP(Set<String> taskIdsHavingRP);
  
  public Set<String> getPersonalTaskIds();
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public String getCurrentUserId();
  public void setCurrentUserId(String currentUserId);
  
  public String getRoleIdOfCurrentUser();
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser);
  
  public ITimeRange getSelectedTimeRange();
  public void setSelectedTimeRange(ITimeRange selectedTimeRange);
}
