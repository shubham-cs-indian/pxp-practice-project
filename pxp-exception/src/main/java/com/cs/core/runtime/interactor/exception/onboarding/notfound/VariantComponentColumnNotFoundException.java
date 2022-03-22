package com.cs.core.runtime.interactor.exception.onboarding.notfound;

public class VariantComponentColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public VariantComponentColumnNotFoundException()
  {
  }
  
  public VariantComponentColumnNotFoundException(
      VariantComponentColumnNotFoundException variantComponentExceptions)
  {
    super();
  }
  
  public VariantComponentColumnNotFoundException(String message)
  {
    super(message);
  }
}
