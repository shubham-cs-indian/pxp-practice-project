package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveEditPermission;

public class UserNotHaveEditPermissionForSupplier extends UserNotHaveEditPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermissionForSupplier()
  {
    super();
  }
  
  public UserNotHaveEditPermissionForSupplier(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
