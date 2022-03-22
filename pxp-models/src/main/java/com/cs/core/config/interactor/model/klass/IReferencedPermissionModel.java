package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IReferencedPermissionModel extends IModel {
  
  public static final String ROLE_PERMISSION = "rolePermission";
  
  public Map<String, IReferencedRolePermissionModel> getRolePermission();
  
  public void setRolePermission(Map<String, IReferencedRolePermissionModel> rolePermission);
}
