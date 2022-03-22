package com.cs.core.config.interactor.model.klass;

public class ModifiedSectionPermissionModel implements IModifiedSectionPermissionModel {
  
  protected String  roleId;
  protected Boolean isCollapsed = false;
  protected Boolean isHidden    = false;
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public Boolean getIsCollapsed()
  {
    return isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
  }
  
  @Override
  public Boolean getIsHidden()
  {
    return isHidden;
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    this.isHidden = isHidden;
  }
}
