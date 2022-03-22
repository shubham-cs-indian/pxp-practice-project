package com.cs.core.config.interactor.exception.endpoint;

import com.cs.core.exception.InvalidTypeException;

public class InvalidTriggeringTypeException  extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTriggeringTypeException()
  {
    super();
  }
  
  public InvalidTriggeringTypeException(InvalidTypeException e)
  {
    super(e);
  }
  
  public InvalidTriggeringTypeException(String message)
  {
    super(message);
  }
}