package com.cs.core.config.interactor.model.klass;

public class ModifiedSectionElementNotificationModel
    implements IModifedSectionElementNotificationModel {
  
  protected String  roleId;
  protected Boolean isNotificationEnabled;
  
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
  public Boolean getIsNotificationEnabled()
  {
    return isNotificationEnabled;
  }
  
  @Override
  public void setIsNotificationEnabled(Boolean isNotificationEnabled)
  {
    this.isNotificationEnabled = isNotificationEnabled;
  }
}
