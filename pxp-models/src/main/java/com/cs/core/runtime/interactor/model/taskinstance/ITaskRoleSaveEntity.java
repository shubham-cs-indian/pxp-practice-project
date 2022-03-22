package com.cs.core.runtime.interactor.model.taskinstance;

import java.io.Serializable;
import java.util.List;

public interface ITaskRoleSaveEntity extends Serializable {
  
  public static final String ADDED_USERS_IDS   = "addedUserIds";
  public static final String DELETED_USERS_IDS = "deletedUserIds";
  
  public static final String ADDED_ROLES_IDS   = "addedRoleIds";
  public static final String DELETED_ROLES_IDS = "deletedRoleIds";
  
  public List<String> getAddedUserIds();
  
  public void setAddedUserIds(List<String> addedUserIds);
  
  public List<String> getDeletedUserIds();
  
  public void setDeletedUserIds(List<String> deletedUserIds);
  
  public List<String> getAddedRoleIds();
  
  public void setAddedRoleIds(List<String> addedRoleIds);
  
  public List<String> getDeletedRoleIds();
  
  public void setDeletedRoleIds(List<String> deletedRoleIds);
}
