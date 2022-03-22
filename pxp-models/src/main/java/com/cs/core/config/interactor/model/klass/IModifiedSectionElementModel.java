package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;

import java.util.List;

public interface IModifiedSectionElementModel extends ISectionElement {
  
  public static final String IS_MODIFIED           = "isModified";
  public static final String MODIFIED_PERMISSION   = "modifiedPermission";
  public static final String MODIFIED_NOTIFICATION = "modifiedNotification";
  
  public Boolean getIsModified();
  
  public void setIsModified(Boolean isModified);
  
  public List<IModifiedSectionElementPermissionModel> getModifiedPermission();
  
  public void setModifiedPermission(
      List<IModifiedSectionElementPermissionModel> modifiedPermission);
  
  public List<IModifedSectionElementNotificationModel> getModifiedNotification();
  
  public void setModifiedNotification(
      List<IModifedSectionElementNotificationModel> modifiedNotification);
}
