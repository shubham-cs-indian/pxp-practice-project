package com.cs.core.config.interactor.exception.pricing;

import com.cs.core.exception.PluginException;

public class InstanceExistsForAttributeException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceExistsForAttributeException()
  {
  }
  
  public InstanceExistsForAttributeException(String message, String errorCode)
  {
    super();
  }
  
  public InstanceExistsForAttributeException(String message)
  {
    super(message);
  }
}
