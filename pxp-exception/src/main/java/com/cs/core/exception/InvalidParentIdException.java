package com.cs.core.exception;

public class InvalidParentIdException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidParentIdException()
  {
    super();
  }
  
  public InvalidParentIdException(Exception e)
  {
    super(e);
  }
}
