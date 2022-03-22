package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.NotFoundException;

public class InstanceVariantNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceVariantNotFoundException()
  {
  }
  
  public InstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
