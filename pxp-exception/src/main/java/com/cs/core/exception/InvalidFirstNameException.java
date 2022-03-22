package com.cs.core.exception;

public class InvalidFirstNameException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidFirstNameException()
  {
    super();
  }
  
  public InvalidFirstNameException(Exception e)
  {
    super(e);
  }
}
