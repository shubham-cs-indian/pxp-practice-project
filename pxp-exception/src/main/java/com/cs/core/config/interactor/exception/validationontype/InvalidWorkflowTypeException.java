package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidWorkflowTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidWorkflowTypeException()
  {
    super();
  }
  
  public InvalidWorkflowTypeException(InvalidTypeException e)
  {
    super(e);
  }

  public InvalidWorkflowTypeException(String message)
  {
    super(message);
  }
}
