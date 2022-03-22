package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class TypeChangedException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public TypeChangedException()
  {
    super();
  }
  
  public TypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
