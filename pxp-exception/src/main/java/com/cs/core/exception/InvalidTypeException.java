package com.cs.core.exception;

public class InvalidTypeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTypeException()
  {
    super();
  }
  
  public InvalidTypeException(Exception e)
  {
    super(e);
  }

  public InvalidTypeException(String message)
  {
    super(message);
  }
}
