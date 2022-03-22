package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;

public class FFMPEGException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public FFMPEGException(Exception e)
  {
    super(e);
  }
  
  public FFMPEGException(String msg)
  {
    super(msg);
  }
}
