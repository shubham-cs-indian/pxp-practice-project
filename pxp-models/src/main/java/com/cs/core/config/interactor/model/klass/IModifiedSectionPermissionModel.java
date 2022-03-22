package com.cs.core.config.interactor.model.klass;

public interface IModifiedSectionPermissionModel {
  
  public static final String ROLE_ID      = "roleId";
  public static final String IS_COLLAPSED = "isCollapsed";
  public static final String IS_HIDDEN    = "isHidden";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsHidden();
  
  public void setIsHidden(Boolean isHidden);
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
}
