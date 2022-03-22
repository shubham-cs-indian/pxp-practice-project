package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveDownloadPermission extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotHaveDownloadPermission()
  {
    super();
  }
  
  public UserNotHaveDownloadPermission(UserNotHaveDownloadPermission e)
  {
    super(e);
  }
}
