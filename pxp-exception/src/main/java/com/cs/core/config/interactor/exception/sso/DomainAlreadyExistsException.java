package com.cs.core.config.interactor.exception.sso;

import com.cs.core.exception.PluginException;

public class DomainAlreadyExistsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public DomainAlreadyExistsException()
  {
  }
  
  public DomainAlreadyExistsException(DomainAlreadyExistsException e)
  {
    super(e);
  }
  
  public DomainAlreadyExistsException(String message)
  {
    super(message);
  }
}
