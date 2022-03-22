package com.cs.core.config.interactor.exception.user;

import com.cs.core.exception.NotFoundException;

public class UserNameFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNameFoundException()
  {
    super();
  }
  
  public UserNameFoundException(UserNameFoundException userNameFoundException)
  {
    super(userNameFoundException);
  }
}
