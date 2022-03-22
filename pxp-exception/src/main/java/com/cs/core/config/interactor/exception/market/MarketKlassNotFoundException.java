package com.cs.core.config.interactor.exception.market;

import com.cs.core.exception.KlassNotFoundException;

public class MarketKlassNotFoundException extends KlassNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public MarketKlassNotFoundException()
  {
  }
  
  public MarketKlassNotFoundException(KlassNotFoundException e)
  {
    super(e);
  }
}
