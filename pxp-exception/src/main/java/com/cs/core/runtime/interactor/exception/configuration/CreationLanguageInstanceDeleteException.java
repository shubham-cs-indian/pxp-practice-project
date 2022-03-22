package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class CreationLanguageInstanceDeleteException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public CreationLanguageInstanceDeleteException()
  {
  }
  
  public CreationLanguageInstanceDeleteException(CreationLanguageInstanceDeleteException e)
  {
    super(e);
  }
  
  public CreationLanguageInstanceDeleteException(String message)
  {
    super(message);
  }
}
