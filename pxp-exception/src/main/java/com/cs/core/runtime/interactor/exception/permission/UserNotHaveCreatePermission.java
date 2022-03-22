package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveCreatePermission extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveCreatePermission()
  {
    super();
  }
  
  public UserNotHaveCreatePermission(UserNotHaveCreatePermission e)
  {
    super(e);
  }
}
