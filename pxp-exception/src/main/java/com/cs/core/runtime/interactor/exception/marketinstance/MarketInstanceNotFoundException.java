package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class MarketInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceNotFoundException()
  {
    super();
  }
  
  public MarketInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
