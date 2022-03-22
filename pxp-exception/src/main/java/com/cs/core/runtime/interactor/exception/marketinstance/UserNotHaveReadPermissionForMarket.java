package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;

public class UserNotHaveReadPermissionForMarket extends UserNotHaveReadPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveReadPermissionForMarket()
  {
    super();
  }
  
  public UserNotHaveReadPermissionForMarket(UserNotHaveReadPermission e)
  {
    super(e);
  }
}
