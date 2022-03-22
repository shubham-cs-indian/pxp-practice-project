package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;


public class JsonConfigurationException extends PluginException {

  private static final long serialVersionUID = 1L;
  
  public JsonConfigurationException()
  {
  }
  
  public JsonConfigurationException(JsonConfigurationException e)
  {
    super(e);
  }
  
  public JsonConfigurationException(String message)
  {
    super(message);
  }
}
