package com.cs.core.config.interactor.exception.variantconfiguration;

import com.cs.core.exception.NotFoundException;

public class VariantConfigurationNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public VariantConfigurationNotFoundException()
  {
    super();
  }
  
  public VariantConfigurationNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
}
