package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidContextTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidContextTypeException()
  {
    super();
  }
  
  public InvalidContextTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
