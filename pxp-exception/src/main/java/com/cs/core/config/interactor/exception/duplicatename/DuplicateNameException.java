package com.cs.core.config.interactor.exception.duplicatename;

import com.cs.core.config.interactor.exception.duplicatecode.DuplicateCodeException;
import com.cs.core.exception.PluginException;

public class DuplicateNameException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DuplicateNameException()
  {
  }
  
  public DuplicateNameException(DuplicateCodeException e)
  {
    super(e);
  }
  
  public DuplicateNameException(String message)
  {
    super(message);
  }
}
