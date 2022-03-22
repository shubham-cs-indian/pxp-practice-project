package com.cs.core.runtime.interactor.entity.taskinstance;

import java.util.ArrayList;
import java.util.List;

public class TaskRoleEntity implements ITaskRoleEntity {
  
  List<String> userIds;
  List<String> roleIds;
  
  public TaskRoleEntity()
  {
  }
  
  public TaskRoleEntity(List<String> userIds)
  {
    setUserIds(userIds);
  }
  
  public TaskRoleEntity(List<String> userIds, List<String> roleIds)
  {
    setUserIds(userIds);
    setRoleIds(roleIds);
  }
  
  @Override
  public List<String> getUserIds()
  {
    if (userIds == null) {
      userIds = new ArrayList<String>();
    }
    return userIds;
  }
  
  @Override
  public void setUserIds(List<String> userIds)
  {
    this.userIds = userIds;
  }
  
  @Override
  public List<String> getRoleIds()
  {
    if (roleIds == null) {
      roleIds = new ArrayList<String>();
    }
    return roleIds;
  }
  
  @Override
  public void setRoleIds(List<String> roleIds)
  {
    this.roleIds = roleIds;
  }
}
