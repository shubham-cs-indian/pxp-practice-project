package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IBulkCreateRoleModel extends IModel {
  
  public List<IRole> getRoles();
  
  public void setRoles(List<IRole> roles);
  
  public Map<String, List<String>> getRoleUsers();
  
  public void setRoleUsers(Map<String, List<String>> roleUsers);
}
