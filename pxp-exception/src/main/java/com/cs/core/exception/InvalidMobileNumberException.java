package com.cs.core.exception;

public class InvalidMobileNumberException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidMobileNumberException()
  {
    super();
  }
  
  public InvalidMobileNumberException(Exception e)
  {
    super(e);
  }
  
}
