package com.cs.core.runtime.interactor.exception.language;

import com.cs.core.exception.PluginException;

public class UserLanguageNotFoundException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UserLanguageNotFoundException()
  {
    super();
  }
  
  public UserLanguageNotFoundException(Exception e)
  {
    super(e);
  }
}
