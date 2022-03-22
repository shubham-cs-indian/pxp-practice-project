package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.Set;

public interface IConfigDetailsOfInstanceTreeForCalendarResponseModel extends IConfigDetailsForGetNewInstanceTreeModel {
  
  public static final String TASK_IDS_HAVING_RP      = "taskIdsHavingRP";
  public static final String PERSONAL_TASK_IDS       = "personalTaskIds";
  public static final String ROLE_ID_OF_CURRENT_USER = "roleIdOfCurrentUser";
  
  public Set<String> getTaskIdsHavingRP();
  public void setTaskIdsHavingRP(Set<String> taskIdsHavingRP);
  
  public Set<String> getPersonalTaskIds();
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public String getRoleIdOfCurrentUser();
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser);
}
