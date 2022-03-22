package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidMappingTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidMappingTypeException()
  {
    super();
  }
  
  public InvalidMappingTypeException(InvalidTypeException e)
  {
    super(e);
  }

  public InvalidMappingTypeException(String message)
  {
    super(message);
  }
}