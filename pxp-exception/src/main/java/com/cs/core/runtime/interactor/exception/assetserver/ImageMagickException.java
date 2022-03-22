package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;

public class ImageMagickException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ImageMagickException(Exception e)
  {
    super(e);
  }
}
