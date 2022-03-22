package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveEditPermission;

public class UserNotHaveEditPermissionForTextAsset extends UserNotHaveEditPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermissionForTextAsset()
  {
    super();
  }
  
  public UserNotHaveEditPermissionForTextAsset(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
