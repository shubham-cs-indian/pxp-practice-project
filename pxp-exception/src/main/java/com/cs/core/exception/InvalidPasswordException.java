package com.cs.core.exception;

public class InvalidPasswordException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidPasswordException()
  {
    super();
  }
  
  public InvalidPasswordException(Exception e)
  {
    super(e);
  }
}
