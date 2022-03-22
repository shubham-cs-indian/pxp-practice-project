package com.cs.core.config.interactor.exception.template;

import com.cs.core.exception.NotFoundException;

public class HeaderNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public HeaderNotFoundException()
  {
  }
  
  public HeaderNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
