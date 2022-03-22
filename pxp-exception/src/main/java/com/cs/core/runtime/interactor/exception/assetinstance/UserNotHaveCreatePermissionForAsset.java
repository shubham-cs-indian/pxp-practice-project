package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForAsset extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForAsset()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForAsset(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
