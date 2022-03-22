package com.cs.core.runtime.strategy.exception.delete;

import java.util.Map;

public class SaveException extends StrategyException {
  
  private static final long serialVersionUID = 1L;
  
  public SaveException(Map responseModel)
  {
    super(responseModel);
  }
}
