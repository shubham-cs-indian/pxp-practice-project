package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.VariantComponentColumnNotFoundException;

public class AttributeValueColumnNotFoundException extends VariantComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AttributeValueColumnNotFoundException()
  {
  }
  
  public AttributeValueColumnNotFoundException(
      AttributeValueColumnNotFoundException attributeValueColumnNotFoundException)
  {
    super();
  }
  
  public AttributeValueColumnNotFoundException(String message)
  {
    super(message);
  }
}
