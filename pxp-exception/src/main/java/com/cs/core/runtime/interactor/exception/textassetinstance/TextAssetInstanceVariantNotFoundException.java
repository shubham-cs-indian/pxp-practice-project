package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceVariantNotFoundException;

public class TextAssetInstanceVariantNotFoundException extends InstanceVariantNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceVariantNotFoundException()
  {
    super();
  }
  
  public TextAssetInstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
