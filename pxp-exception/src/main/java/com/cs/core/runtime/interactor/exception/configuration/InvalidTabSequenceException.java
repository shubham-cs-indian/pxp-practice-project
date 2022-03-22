package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class InvalidTabSequenceException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidTabSequenceException()
  {
  }
  
  public InvalidTabSequenceException(InvalidTabSequenceException e)
  {
    super(e);
  }
  
  public InvalidTabSequenceException(String message)
  {
    super(message);
  }
}
