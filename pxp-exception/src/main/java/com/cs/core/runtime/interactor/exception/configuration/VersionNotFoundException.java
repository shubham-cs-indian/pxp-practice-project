package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class VersionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public VersionNotFoundException()
  {
  }
  
  public VersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
