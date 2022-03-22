package com.cs.core.config.interactor.model.klass;

public interface IModifedSectionElementNotificationModel {
  
  public static final String ROLE_ID                 = "roleId";
  public static final String IS_NOTIFICATION_ENABLED = "isNotificationEnabled";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsNotificationEnabled();
  
  public void setIsNotificationEnabled(Boolean isNotificationEnabled);
}
