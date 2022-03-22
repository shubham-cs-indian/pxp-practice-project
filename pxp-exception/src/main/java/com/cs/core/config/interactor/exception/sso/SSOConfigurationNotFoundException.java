package com.cs.core.config.interactor.exception.sso;

import com.cs.core.exception.NotFoundException;

public class SSOConfigurationNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SSOConfigurationNotFoundException()
  {
    super();
  }
  
  public SSOConfigurationNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
