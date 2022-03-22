package com.cs.core.runtime.interactor.exception.assetserver;

import com.cs.core.exception.PluginException;

public class Docx4JException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public Docx4JException(Exception e)
  {
    super(e);
  }
}
