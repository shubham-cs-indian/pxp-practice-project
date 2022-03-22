package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.exception.PluginException;

public class SubsetVariantExistsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public SubsetVariantExistsException()
  {
  }
  
  public SubsetVariantExistsException(SubsetVariantExistsException e)
  {
    super(e);
  }
}
