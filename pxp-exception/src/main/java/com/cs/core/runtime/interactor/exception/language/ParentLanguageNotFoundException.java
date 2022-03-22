package com.cs.core.runtime.interactor.exception.language;

import com.cs.core.exception.NotFoundException;

public class ParentLanguageNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentLanguageNotFoundException()
  {
    super();
  }
  
  public ParentLanguageNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
