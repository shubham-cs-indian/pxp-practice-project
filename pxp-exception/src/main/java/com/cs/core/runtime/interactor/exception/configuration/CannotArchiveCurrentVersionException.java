package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class CannotArchiveCurrentVersionException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public CannotArchiveCurrentVersionException()
  {
  }
  
  public CannotArchiveCurrentVersionException(CannotArchiveCurrentVersionException e)
  {
    super(e);
  }
  
  public CannotArchiveCurrentVersionException(String message)
  {
    super(message);
  }
}
