package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.role.IRole;

import java.util.List;
import java.util.Map;

public class BulkCreateRoleModel implements IBulkCreateRoleModel {
  
  List<IRole>               roles;
  
  Map<String, List<String>> roleUsers;
  
  @Override
  public List<IRole> getRoles()
  {
    return roles;
  }
  
  @Override
  public void setRoles(List<IRole> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public Map<String, List<String>> getRoleUsers()
  {
    return roleUsers;
  }
  
  @Override
  public void setRoleUsers(Map<String, List<String>> roleUsers)
  {
    this.roleUsers = roleUsers;
  }
}
