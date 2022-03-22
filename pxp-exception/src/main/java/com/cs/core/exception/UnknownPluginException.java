package com.cs.core.exception;

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
