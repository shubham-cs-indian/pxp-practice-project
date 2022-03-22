package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.configuration.TypeChangedException;

public class MarketInstanceTypeChangedException extends TypeChangedException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceTypeChangedException()
  {
    super();
  }
  
  public MarketInstanceTypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
