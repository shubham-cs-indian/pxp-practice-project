package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveEditPermission extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveEditPermission()
  {
    super();
  }
  
  public UserNotHaveEditPermission(UserNotHaveEditPermission e)
  {
    super(e);
  }
}
