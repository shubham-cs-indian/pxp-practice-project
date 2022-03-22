package com.cs.core.config.interactor.exception.icon;

import com.cs.core.exception.NotFoundException;

public class IconNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public IconNotFoundException()
  {
  }
  
  public IconNotFoundException(String message, String errorCode)
  {
    super();
  }
  
  public IconNotFoundException(String message)
  {
    super(message);
  }
  
  public IconNotFoundException(NotFoundException e)
  {
      super(e);
  }
}
