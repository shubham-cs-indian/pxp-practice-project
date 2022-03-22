package com.cs.core.exception;

public class InvalidCodeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidCodeException()
  {
    super();
  }
  
  public InvalidCodeException(Exception e)
  {
    super(e);
  }
}
