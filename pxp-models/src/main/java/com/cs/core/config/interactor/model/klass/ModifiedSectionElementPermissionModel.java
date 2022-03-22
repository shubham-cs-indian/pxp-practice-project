package com.cs.core.config.interactor.model.klass;

public class ModifiedSectionElementPermissionModel
    implements IModifiedSectionElementPermissionModel {
  
  protected String  roleId;
  protected Boolean isDisabled = false;
  
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
  public Boolean getIsDisabled()
  {
    return isDisabled;
  }
  
  @Override
  public void setIsDisabled(Boolean isDisabled)
  {
    this.isDisabled = isDisabled;
  }
}
