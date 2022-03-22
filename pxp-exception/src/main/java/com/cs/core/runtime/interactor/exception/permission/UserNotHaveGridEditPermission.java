package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveGridEditPermission extends PluginException{
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveGridEditPermission()
  {
    super();
  }
  
  public UserNotHaveGridEditPermission(UserNotHaveGridEditPermission e)
  {
    super(e);
  }
}
