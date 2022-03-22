package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;

public class UserNotHaveReadPermissionForTextAsset extends UserNotHaveReadPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveReadPermissionForTextAsset()
  {
    super();
  }
  
  public UserNotHaveReadPermissionForTextAsset(UserNotHaveReadPermission e)
  {
    super(e);
  }
}
