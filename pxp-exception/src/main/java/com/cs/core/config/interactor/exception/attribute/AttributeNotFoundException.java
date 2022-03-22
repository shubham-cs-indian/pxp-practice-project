package com.cs.core.config.interactor.exception.attribute;

import com.cs.core.exception.NotFoundException;

public class AttributeNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AttributeNotFoundException()
  {
  }
  
  public AttributeNotFoundException(String message, String errorCode)
  {
    super();
  }
  
  public AttributeNotFoundException(String message)
  {
    super(message);
  }
}
