package com.cs.core.exception;

public class ModuleEntityNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ModuleEntityNotFoundException()
  {
  }
  
  public ModuleEntityNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public ModuleEntityNotFoundException(String message)
  {
    super(message);
  }
  
}
