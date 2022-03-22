package com.cs.core.exception;

public class InvalidLocaleIdException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidLocaleIdException()
  {
    super();
  }
  
  public InvalidLocaleIdException(Exception e)
  {
    super(e);
  }
}
