package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.permission.UserNotHaveEditPermission;

public class UserNotHaveEditPermissionForMarket extends UserNotHaveEditPermission {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermissionForMarket()
  {
    super();
  }
  
  public UserNotHaveEditPermissionForMarket(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
