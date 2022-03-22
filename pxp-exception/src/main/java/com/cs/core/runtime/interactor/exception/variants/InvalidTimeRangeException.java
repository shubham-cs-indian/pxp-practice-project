package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.exception.PluginException;

public class InvalidTimeRangeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTimeRangeException()
  {
  }
  
  public InvalidTimeRangeException(InvalidTimeRangeException e)
  {
    super(e);
  }
  
  public InvalidTimeRangeException(String message)
  {
    super(message);
  }
}
