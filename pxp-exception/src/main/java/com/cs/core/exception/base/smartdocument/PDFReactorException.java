package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;

public class PDFReactorException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public PDFReactorException()
  {
  }
  
  public PDFReactorException(PDFReactorException e)
  {
    super(e);
  }
  
  
  public PDFReactorException(String message)
  {
    super(message);
  }
}
