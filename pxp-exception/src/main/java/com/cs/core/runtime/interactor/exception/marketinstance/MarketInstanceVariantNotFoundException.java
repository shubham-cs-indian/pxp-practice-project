package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceVariantNotFoundException;

public class MarketInstanceVariantNotFoundException extends InstanceVariantNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceVariantNotFoundException()
  {
    super();
  }
  
  public MarketInstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
