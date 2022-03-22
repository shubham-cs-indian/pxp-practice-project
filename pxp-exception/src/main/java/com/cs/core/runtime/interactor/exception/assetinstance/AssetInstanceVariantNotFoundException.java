package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceVariantNotFoundException;

public class AssetInstanceVariantNotFoundException extends InstanceVariantNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceVariantNotFoundException()
  {
    super();
  }
  
  public AssetInstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
