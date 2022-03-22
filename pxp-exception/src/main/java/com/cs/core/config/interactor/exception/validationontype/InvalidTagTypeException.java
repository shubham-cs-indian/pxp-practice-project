package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidTagTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTagTypeException()
  {
    super();
  }
  
  public InvalidTagTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
