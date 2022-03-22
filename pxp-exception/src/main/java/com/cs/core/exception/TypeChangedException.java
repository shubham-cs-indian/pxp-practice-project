package com.cs.core.exception;

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
