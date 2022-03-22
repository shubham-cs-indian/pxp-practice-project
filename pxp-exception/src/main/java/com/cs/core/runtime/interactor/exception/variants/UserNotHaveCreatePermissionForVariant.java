package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForVariant extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForVariant()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForVariant(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
