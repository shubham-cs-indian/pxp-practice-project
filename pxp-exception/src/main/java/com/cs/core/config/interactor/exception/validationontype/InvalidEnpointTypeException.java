package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidEnpointTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidEnpointTypeException()
  {
    super();
  }
  
  public InvalidEnpointTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
