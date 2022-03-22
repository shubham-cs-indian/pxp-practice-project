package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidEventTypeInWorkFlowException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidEventTypeInWorkFlowException()
  {
    super();
  }
  
  public InvalidEventTypeInWorkFlowException(InvalidTypeException e)
  {
    super(e);
  }
}
