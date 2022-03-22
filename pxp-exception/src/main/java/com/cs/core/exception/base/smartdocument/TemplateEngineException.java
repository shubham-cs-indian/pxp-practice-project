package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;

public class TemplateEngineException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public TemplateEngineException()
  {
  }
  
  public TemplateEngineException(TemplateEngineException e)
  {
    super(e);
  }
  
  
  public TemplateEngineException(String message)
  {
    super(message);
  }
}
