package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;

public class UserNotHaveReadPermissionForSupplier extends UserNotHaveReadPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveReadPermissionForSupplier()
  {
    super();
  }
  
  public UserNotHaveReadPermissionForSupplier(UserNotHaveReadPermission e)
  {
    super(e);
  }
}
