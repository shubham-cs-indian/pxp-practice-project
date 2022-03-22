package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

public class PermissionsForEntityStatusModel implements IPermissionsForEntityStatusModel {

  
  private static final long serialVersionUID = 1L;
  
  private List<String> unAuthorizedUsersOrRoles;

  
  public List<String> getUnAuthorizedUsersOrRoles()
  {
    return unAuthorizedUsersOrRoles;
  }

  
  public void setUnAuthorizedUsersOrRoles(List<String> unAuthorizedUsersOrRoles)
  {
    this.unAuthorizedUsersOrRoles = unAuthorizedUsersOrRoles;
  }
  
  
  
}
