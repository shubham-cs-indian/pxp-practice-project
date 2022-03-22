package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class AssetInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceNotFoundException()
  {
    super();
  }
  
  public AssetInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
