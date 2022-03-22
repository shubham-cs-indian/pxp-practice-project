package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidTaxonomyTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTaxonomyTypeException()
  {
    super();
  }
  
  public InvalidTaxonomyTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
