package com.cs.core.exception;

public class NotFoundException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public NotFoundException()
  {
  }
  
  public NotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public NotFoundException(String message)
  {
    super(message);
  }
}
