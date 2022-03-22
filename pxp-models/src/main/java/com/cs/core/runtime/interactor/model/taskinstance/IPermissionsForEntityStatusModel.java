package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPermissionsForEntityStatusModel extends IModel {
  
  public static final String UNAUTHORIZED_USERS_OR_ROLES             = "unAuthorizedUsersOrRoles";
  
  public List<String> getUnAuthorizedUsersOrRoles();
  public void setUnAuthorizedUsersOrRoles(List<String> unAuthorizedUsersOrRoles);
}
