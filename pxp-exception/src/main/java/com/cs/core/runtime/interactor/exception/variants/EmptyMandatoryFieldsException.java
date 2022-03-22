package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.exception.PluginException;

public class EmptyMandatoryFieldsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public EmptyMandatoryFieldsException()
  {
  }
  
  public EmptyMandatoryFieldsException(EmptyMandatoryFieldsException e)
  {
    super(e);
  }
  
  public EmptyMandatoryFieldsException(String message)
  {
    super(message);
  }
}
