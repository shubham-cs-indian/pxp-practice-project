package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidNatureTypeKlassException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidNatureTypeKlassException()
  {
    super();
  }
  
  public InvalidNatureTypeKlassException(InvalidTypeException e)
  {
    super(e);
  }
}
