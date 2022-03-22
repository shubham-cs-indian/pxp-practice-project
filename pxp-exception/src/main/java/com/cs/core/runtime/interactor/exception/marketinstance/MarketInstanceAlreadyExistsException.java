package com.cs.core.runtime.interactor.exception.marketinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class MarketInstanceAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketInstanceAlreadyExistsException()
  {
    super();
  }
  
  public MarketInstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
