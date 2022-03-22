package com.cs.core.runtime.interactor.exception.permission;

import com.cs.core.exception.NotFoundException;

public class GlobalPermissionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public GlobalPermissionNotFoundException()
  {
  }
  
  public GlobalPermissionNotFoundException(String message, String errorCode)
  {
    super();
  }
}
