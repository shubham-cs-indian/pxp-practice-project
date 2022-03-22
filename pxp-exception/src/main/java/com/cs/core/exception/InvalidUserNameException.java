package com.cs.core.exception;

public class InvalidUserNameException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidUserNameException()
  {
    super();
  }
  
  public InvalidUserNameException(Exception e)
  {
    super(e);
  }
}
