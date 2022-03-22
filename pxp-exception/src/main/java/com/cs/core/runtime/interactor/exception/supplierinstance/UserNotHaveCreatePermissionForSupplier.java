package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForSupplier extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForSupplier()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForSupplier(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
