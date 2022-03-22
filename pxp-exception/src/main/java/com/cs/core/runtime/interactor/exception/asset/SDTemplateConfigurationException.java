package com.cs.core.runtime.interactor.exception.asset;

import com.cs.core.exception.PluginException;


public class SDTemplateConfigurationException extends PluginException {

  private static final long serialVersionUID = 1L;
  
  public SDTemplateConfigurationException()
  {
  }
  
  public SDTemplateConfigurationException(SDTemplateConfigurationException e)
  {
    super(e);
  }
  
  public SDTemplateConfigurationException(String message)
  {
    super(message);
  }
}
