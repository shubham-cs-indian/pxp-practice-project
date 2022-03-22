package com.cs.core.exception;


public class InvalidInfoException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidInfoException()
  {
    super();
  }
  
  public InvalidInfoException(Exception e)
  {
    super(e);
  }
  
  public InvalidInfoException(String string)
  {
    super(string);
  }
}
