package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class AttributeMultiSelectException extends MultiSelectException {
  
  private static final long serialVersionUID = 1L;
  
  public AttributeMultiSelectException()
  {
  }
  
  public AttributeMultiSelectException(AttributeMultiSelectException attributeMultiSelectExceptions)
  {
    super();
  }
  
  public AttributeMultiSelectException(String message)
  {
    super(message);
  }
}
