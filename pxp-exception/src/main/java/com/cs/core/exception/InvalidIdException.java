package com.cs.core.exception;

public class InvalidIdException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidIdException()
  {
    super();
  }
  
  public InvalidIdException(Exception e)
  {
    super(e);
  }
}
