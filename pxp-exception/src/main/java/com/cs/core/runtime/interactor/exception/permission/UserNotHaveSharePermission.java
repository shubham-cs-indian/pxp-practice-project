package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveSharePermission extends PluginException{
  
  private static final long serialVersionUID = 1L;

  public UserNotHaveSharePermission()
  {
    super();
  }
  
  public UserNotHaveSharePermission(UserNotHaveSharePermission e)
  {
    super(e);
  }
}
