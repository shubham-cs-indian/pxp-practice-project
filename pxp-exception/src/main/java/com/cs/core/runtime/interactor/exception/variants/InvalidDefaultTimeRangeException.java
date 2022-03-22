package com.cs.core.runtime.interactor.exception.variants;

public class InvalidDefaultTimeRangeException extends InvalidTimeRangeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidDefaultTimeRangeException()
  {
  }
  
  public InvalidDefaultTimeRangeException(InvalidDefaultTimeRangeException e)
  {
    super(e);
  }
  
  public InvalidDefaultTimeRangeException(String message)
  {
    super(message);
  }
}
