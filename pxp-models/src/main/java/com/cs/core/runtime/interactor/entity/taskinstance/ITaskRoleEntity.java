package com.cs.core.runtime.interactor.entity.taskinstance;

import java.util.List;

public interface ITaskRoleEntity {
  
  public static final String USERS_IDS = "userIds";
  public static final String ROLES_IDS = "roleIds";
  
  public List<String> getUserIds();
  
  public void setUserIds(List<String> userIds);
  
  public List<String> getRoleIds();
  
  public void setRoleIds(List<String> roleIds);
}
