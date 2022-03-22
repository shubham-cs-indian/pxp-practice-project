package com.cs.core.runtime.interactor.exception.endpoint;

import com.cs.core.exception.PluginException;

public class InvalidEndpointIdException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidEndpointIdException()
  {
  }
  
  public InvalidEndpointIdException(PluginException e)
  {
    super(e);
  }
}
