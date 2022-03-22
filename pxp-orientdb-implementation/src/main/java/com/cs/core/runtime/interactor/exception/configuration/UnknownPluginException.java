package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class UnknownPluginException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UnknownPluginException()
  {
    super("Unknown Plugin Exception");
  }
  
  public UnknownPluginException(String message)
  {
    super(message);
  }
}
