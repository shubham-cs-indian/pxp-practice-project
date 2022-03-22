package com.cs.core.runtime.interactor.exception.onboarding.column;

public class AttributeIdNotFoundException extends PrimaryKeyColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AttributeIdNotFoundException()
  {
  }
  
  public AttributeIdNotFoundException(AttributeIdNotFoundException attributeIdNotFoundException)
  {
    super();
  }
  
  public AttributeIdNotFoundException(String message)
  {
    super(message);
  }
}
