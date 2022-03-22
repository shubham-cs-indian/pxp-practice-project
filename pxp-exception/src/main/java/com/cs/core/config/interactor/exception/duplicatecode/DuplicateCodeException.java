package com.cs.core.config.interactor.exception.duplicatecode;

import com.cs.core.exception.PluginException;

public class DuplicateCodeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DuplicateCodeException()
  {
  }
  
  public DuplicateCodeException(DuplicateCodeException e)
  {
    super(e);
  }
  
  public DuplicateCodeException(String message)
  {
    super(message);
  }
}
