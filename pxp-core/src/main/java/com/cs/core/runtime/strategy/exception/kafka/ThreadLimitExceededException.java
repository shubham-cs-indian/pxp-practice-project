package com.cs.core.runtime.strategy.exception.kafka;

import com.cs.core.exception.PluginException;

public class ThreadLimitExceededException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ThreadLimitExceededException()
  {
  }
  
  public ThreadLimitExceededException(PluginException e)
  {
    super(e);
  }
}
