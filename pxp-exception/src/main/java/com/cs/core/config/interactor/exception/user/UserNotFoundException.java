package com.cs.core.config.interactor.exception.user;

import com.cs.core.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNotFoundException()
  {
    super();
  }
  
  public UserNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
