package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveExportPermission extends PluginException {
  
 private static final long serialVersionUID = 1L;
  
  public UserNotHaveExportPermission()
  {
    super();
  }
  
  public UserNotHaveExportPermission(UserNotHaveExportPermission e)
  {
    super(e);
  }
}
