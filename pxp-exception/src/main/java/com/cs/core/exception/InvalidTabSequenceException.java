package com.cs.core.exception;

public class InvalidTabSequenceException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTabSequenceException()
  {
  }
  
  public InvalidTabSequenceException(InvalidTabSequenceException e)
  {
    super(e);
  }
  
  public InvalidTabSequenceException(String message)
  {
    super(message);
  }
}
