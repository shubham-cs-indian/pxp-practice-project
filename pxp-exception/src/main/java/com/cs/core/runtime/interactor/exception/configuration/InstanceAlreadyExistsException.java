package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class InstanceAlreadyExistsException extends PluginException {
  
  public InstanceAlreadyExistsException()
  {
  }
  
  public InstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
  
  private static final long serialVersionUID = 1L;
}
