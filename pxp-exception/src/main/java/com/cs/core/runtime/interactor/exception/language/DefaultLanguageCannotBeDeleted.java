package com.cs.core.runtime.interactor.exception.language;

import com.cs.core.exception.PluginException;

public class DefaultLanguageCannotBeDeleted extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DefaultLanguageCannotBeDeleted()
  {
    super();
  }
  
  public DefaultLanguageCannotBeDeleted(PluginException e)
  {
    super(e);
  }
}
