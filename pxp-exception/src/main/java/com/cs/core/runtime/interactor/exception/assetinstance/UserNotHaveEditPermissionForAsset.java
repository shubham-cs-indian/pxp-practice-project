package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveEditPermission;

public class UserNotHaveEditPermissionForAsset extends UserNotHaveEditPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermissionForAsset()
  {
    super();
  }
  
  public UserNotHaveEditPermissionForAsset(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
