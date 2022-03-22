package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class ParentNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ParentNotFoundException()
  {
  }
  
  public ParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
