package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class ModuleEntityNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ModuleEntityNotFoundException()
  {
  }
  
  public ModuleEntityNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
