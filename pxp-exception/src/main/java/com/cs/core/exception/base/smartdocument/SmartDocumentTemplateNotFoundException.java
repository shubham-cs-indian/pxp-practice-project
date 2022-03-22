package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;


/**
 * This exception is thrown when Smart Document Template is not found on swift server.
 * @author vannya.kalani
 *
 */
public class SmartDocumentTemplateNotFoundException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public SmartDocumentTemplateNotFoundException()
  {
  }
  
  public SmartDocumentTemplateNotFoundException(SmartDocumentTemplateNotFoundException e)
  {
    super(e);
  }
  
  public SmartDocumentTemplateNotFoundException(String message)
  {
    super(message);
  }
}
