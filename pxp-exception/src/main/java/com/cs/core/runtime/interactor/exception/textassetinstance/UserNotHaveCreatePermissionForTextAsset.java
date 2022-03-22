package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForTextAsset extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForTextAsset()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForTextAsset(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
