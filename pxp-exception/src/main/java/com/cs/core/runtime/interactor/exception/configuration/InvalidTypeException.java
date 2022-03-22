package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class InvalidTypeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTypeException()
  {
    super();
  }
  
  public InvalidTypeException(Exception e)
  {
    super(e);
  }
}
