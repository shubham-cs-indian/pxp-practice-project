package com.cs.core.exception;

public class InstanceNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceNotFoundException()
  {
    super();
  }
  
  public InstanceNotFoundException(String string)
  {
    super(string);
  }
  
  public InstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
