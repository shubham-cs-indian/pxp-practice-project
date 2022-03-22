package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.configuration.VersionNotFoundException;

public class MarketInstanceVersionNotFoundException extends VersionNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceVersionNotFoundException()
  {
    super();
  }
  
  public MarketInstanceVersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
