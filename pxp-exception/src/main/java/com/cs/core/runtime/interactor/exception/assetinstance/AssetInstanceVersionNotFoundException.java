package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.VersionNotFoundException;

public class AssetInstanceVersionNotFoundException extends VersionNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceVersionNotFoundException()
  {
    super();
  }
  
  public AssetInstanceVersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
