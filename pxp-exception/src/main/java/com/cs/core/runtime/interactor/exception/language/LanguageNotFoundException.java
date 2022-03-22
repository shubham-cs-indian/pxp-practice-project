package com.cs.core.runtime.interactor.exception.language;

import com.cs.core.exception.NotFoundException;

public class LanguageNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public LanguageNotFoundException()
  {
    super();
  }
  
  public LanguageNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
