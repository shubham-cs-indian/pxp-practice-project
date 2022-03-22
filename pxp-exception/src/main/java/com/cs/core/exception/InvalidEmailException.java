package com.cs.core.exception;

public class InvalidEmailException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidEmailException()
  {
    super();
  }
  
  public InvalidEmailException(Exception e)
  {
    super(e);
  }
}
