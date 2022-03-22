package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;

public class ProductLanguageNotAvailableException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ProductLanguageNotAvailableException()
  {
  }
  
  public ProductLanguageNotAvailableException(ProductLanguageNotAvailableException e)
  {
    super(e);
  }
  
  
  public ProductLanguageNotAvailableException(String message)
  {
    super(message);
  }
}
