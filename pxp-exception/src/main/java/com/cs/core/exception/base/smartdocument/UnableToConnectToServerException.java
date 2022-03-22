package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;

public class UnableToConnectToServerException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public UnableToConnectToServerException()
  {
  }
  
  public UnableToConnectToServerException(UnableToConnectToServerException e)
  {
    super(e);
  }
  
  public UnableToConnectToServerException(String message)
  {
    super(message);
  }
}