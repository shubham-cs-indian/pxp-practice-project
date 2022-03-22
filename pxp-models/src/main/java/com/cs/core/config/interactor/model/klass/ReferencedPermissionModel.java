package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ReferencedPermissionModel implements IReferencedPermissionModel {
  
  protected Map<String, IReferencedRolePermissionModel> rolePermission;
  
  @Override
  public Map<String, IReferencedRolePermissionModel> getRolePermission()
  {
    return rolePermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRolePermissionModel.class)
  public void setRolePermission(Map<String, IReferencedRolePermissionModel> rolePermission)
  {
    this.rolePermission = rolePermission;
  }
}
