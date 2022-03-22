package com.cs.core.config.interactor.exception.template;

import com.cs.core.exception.NotFoundException;

public class SequenceNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SequenceNotFoundException()
  {
  }
  
  public SequenceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
