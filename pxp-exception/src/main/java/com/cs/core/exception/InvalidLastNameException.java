package com.cs.core.exception;


public class InvalidLastNameException extends PluginException {
  
private static final long serialVersionUID = 1L;
  
  public InvalidLastNameException()
  {
    super();
  }
  
  public InvalidLastNameException(Exception e)
  {
    super(e);
  }
}
