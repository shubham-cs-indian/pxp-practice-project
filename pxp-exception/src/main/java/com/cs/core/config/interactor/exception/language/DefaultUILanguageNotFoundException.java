package com.cs.core.config.interactor.exception.language;

import com.cs.core.exception.NotFoundException;

public class DefaultUILanguageNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public DefaultUILanguageNotFoundException()
  {
    super();
  }
  
  public DefaultUILanguageNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public DefaultUILanguageNotFoundException(String message)
  {
    super(message);
  }
}
