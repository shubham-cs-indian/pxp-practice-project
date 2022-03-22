package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;


public class TemplateConfigurationException extends PluginException {

  private static final long serialVersionUID = 1L;
  
  public TemplateConfigurationException()
  {
  }
  
  public TemplateConfigurationException(TemplateConfigurationException e)
  {
    super(e);
  }
  
  public TemplateConfigurationException(String message)
  {
    super(message);
  }
}
