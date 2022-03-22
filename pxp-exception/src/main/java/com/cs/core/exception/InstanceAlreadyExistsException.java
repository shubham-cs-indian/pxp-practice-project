package com.cs.core.exception;

public class InstanceAlreadyExistsException extends PluginException {
  
  public InstanceAlreadyExistsException()
  {
  }
  
  public InstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
  
  private static final long serialVersionUID = 1L;
}
