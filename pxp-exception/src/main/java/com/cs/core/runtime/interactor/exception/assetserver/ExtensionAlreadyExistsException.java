package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;

public class ExtensionAlreadyExistsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ExtensionAlreadyExistsException(Exception e)
  {
    super(e);
  }
  
  public ExtensionAlreadyExistsException(String msg)
  {
    super(msg);
  }
}
