package com.cs.core.runtime.strategy.exception.delete;

import java.util.Map;

public class SaveConflictException extends StrategyException {
  
  private static final long serialVersionUID = 1L;
  
  public SaveConflictException(Map responseModel)
  {
    super(responseModel);
  }
}
