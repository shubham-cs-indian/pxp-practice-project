package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveImportPermission extends PluginException{

  private static final long serialVersionUID = 1L;

  public UserNotHaveImportPermission()
  {
    super();
  }
  
  public UserNotHaveImportPermission(UserNotHaveImportPermission e)
  {
    super(e);
  }
}
