package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidEventTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidEventTypeException()
  {
    super();
  }
  
  public InvalidEventTypeException(InvalidTypeException e)
  {
    super(e);
  }

  public InvalidEventTypeException(String message)
  {
    super(message);
  }
}
