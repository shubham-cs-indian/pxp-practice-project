package com.cs.core.runtime.strategy.exception.delete;

import java.util.List;
import java.util.Map;

public class DeleteException extends StrategyException {
  
  private static final long serialVersionUID = 1L;
  
  public DeleteException(Map<String, List<String>> responseModel)
  {
    super(responseModel);
  }
}
