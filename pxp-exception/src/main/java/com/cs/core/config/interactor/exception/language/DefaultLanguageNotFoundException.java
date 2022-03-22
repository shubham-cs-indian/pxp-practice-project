package com.cs.core.config.interactor.exception.language;

import com.cs.core.exception.NotFoundException;

public class DefaultLanguageNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public DefaultLanguageNotFoundException()
  {
    super();
  }
  
  public DefaultLanguageNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public DefaultLanguageNotFoundException(String message)
  {
    super(message);
  }
}
