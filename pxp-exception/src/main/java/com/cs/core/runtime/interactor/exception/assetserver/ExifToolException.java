package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;

public class ExifToolException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ExifToolException(Exception e)
  {
    super(e);
  }
  
  public ExifToolException(String msg)
  {
    super(msg);
  }
}
