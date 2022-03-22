package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.PluginException;

public class UserNotHaveBulkEditPermission extends PluginException{

  private static final long serialVersionUID = 1L;
  
  public UserNotHaveBulkEditPermission()
  {
    super();
  }
  
  public UserNotHaveBulkEditPermission(UserNotHaveBulkEditPermission e)
  {
    super(e);
  }
}
