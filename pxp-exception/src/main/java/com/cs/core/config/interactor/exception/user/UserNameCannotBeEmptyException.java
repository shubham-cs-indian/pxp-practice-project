package com.cs.core.config.interactor.exception.user;

import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;

public class UserNameCannotBeEmptyException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UserNameCannotBeEmptyException()
  {
    super();
  }
  
  public UserNameCannotBeEmptyException(NotFoundException e)
  {
    super(e);
  }
}
