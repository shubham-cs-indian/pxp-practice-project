package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;

public class UserNotHaveCreatePermissionForMarket extends UserNotHaveCreatePermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermissionForMarket()
  {
    super();
  }
  
  public UserNotHaveCreatePermissionForMarket(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
