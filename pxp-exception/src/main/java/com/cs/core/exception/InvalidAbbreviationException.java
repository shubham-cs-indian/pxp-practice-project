package com.cs.core.exception;

public class InvalidAbbreviationException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidAbbreviationException()
  {
    super();
  }
  
  public InvalidAbbreviationException(Exception e)
  {
    super(e);
  }
}
