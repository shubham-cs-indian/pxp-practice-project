package com.cs.core.config.interactor.exception.user;

import com.cs.core.exception.PluginException;

public class MandatoryFieldException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public MandatoryFieldException()
  {
    super();
  }
  
  public MandatoryFieldException(String e)
  {
    super(e);
  }
}
