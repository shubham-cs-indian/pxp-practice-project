package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidTasksTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTasksTypeException()
  {
    super();
  }
  
  public InvalidTasksTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
