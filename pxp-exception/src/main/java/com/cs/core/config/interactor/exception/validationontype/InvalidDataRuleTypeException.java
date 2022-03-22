package com.cs.core.config.interactor.exception.validationontype;

import com.cs.core.exception.InvalidTypeException;

public class InvalidDataRuleTypeException extends InvalidTypeException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidDataRuleTypeException()
  {
    super();
  }
  
  public InvalidDataRuleTypeException(InvalidTypeException e)
  {
    super(e);
  }
}
