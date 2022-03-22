package com.cs.core.exception;

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
