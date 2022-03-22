package com.cs.core.exception;

public class InvalidBaseTypeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidBaseTypeException()
  {
    super();
  }
  
  public InvalidBaseTypeException(Exception e)
  {
    super(e);
  }

  public InvalidBaseTypeException(String message)
  {
    super(message);
  }
}
