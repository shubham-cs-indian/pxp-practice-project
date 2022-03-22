package com.cs.core.config.interactor.exception.asset;

import com.cs.core.exception.KlassNotFoundException;

public class AssetKlassNotFoundException extends KlassNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetKlassNotFoundException()
  {
  }
  
  public AssetKlassNotFoundException(KlassNotFoundException e)
  {
    super(e);
  }
}
