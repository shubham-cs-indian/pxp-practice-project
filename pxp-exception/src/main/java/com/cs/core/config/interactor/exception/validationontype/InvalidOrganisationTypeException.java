package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidOrganisationTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidOrganisationTypeException()
  {
    super();
  }
  
  public InvalidOrganisationTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
