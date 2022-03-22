package com.cs.core.config.interactor.model.klass;

public interface IModifiedSectionElementPermissionModel {
  
  public static final String ROLE_ID     = "roleId";
  public static final String IS_DISABLED = "isDisabled";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsDisabled();
  
  public void setIsDisabled(Boolean isDisabled);
}
