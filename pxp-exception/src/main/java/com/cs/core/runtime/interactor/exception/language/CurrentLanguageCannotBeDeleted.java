package com.cs.core.runtime.interactor.exception.language;

import com.cs.core.exception.PluginException;

public class CurrentLanguageCannotBeDeleted extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public CurrentLanguageCannotBeDeleted()
  {
    super();
  }
  
  public CurrentLanguageCannotBeDeleted(PluginException e)
  {
    super(e);
  }
}
