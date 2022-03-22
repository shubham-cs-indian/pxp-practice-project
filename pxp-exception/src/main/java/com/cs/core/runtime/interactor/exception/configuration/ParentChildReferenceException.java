package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class ParentChildReferenceException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentChildReferenceException()
  {
  }
  
  public ParentChildReferenceException(NotFoundException e)
  {
    super(e);
  }
}
