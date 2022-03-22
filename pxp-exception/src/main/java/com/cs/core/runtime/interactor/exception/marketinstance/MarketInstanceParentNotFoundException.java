package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class MarketInstanceParentNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceParentNotFoundException()
  {
    super();
  }
  
  public MarketInstanceParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
