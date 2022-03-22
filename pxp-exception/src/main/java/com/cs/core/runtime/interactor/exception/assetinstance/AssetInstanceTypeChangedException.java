package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.TypeChangedException;

public class AssetInstanceTypeChangedException extends TypeChangedException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceTypeChangedException()
  {
    super();
  }
  
  public AssetInstanceTypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
