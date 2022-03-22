package com.cs.core.exception;

public class DuplicateUserNameException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DuplicateUserNameException()
  {
    super();
  }
  
  public DuplicateUserNameException(Exception e)
  {
    super(e);
  }
}
