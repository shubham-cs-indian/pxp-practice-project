package com.cs.core.config.interactor.exception.language;

import com.cs.core.exception.NotFoundException;

public class DefaultDataLanguageNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public DefaultDataLanguageNotFoundException()
  {
    super();
  }
  
  public DefaultDataLanguageNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public DefaultDataLanguageNotFoundException(String message)
  {
    super(message);
  }
}
