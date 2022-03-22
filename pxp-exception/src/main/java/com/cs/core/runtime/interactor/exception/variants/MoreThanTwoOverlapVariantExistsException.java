package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.exception.PluginException;

public class MoreThanTwoOverlapVariantExistsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public MoreThanTwoOverlapVariantExistsException()
  {
  }
  
  public MoreThanTwoOverlapVariantExistsException(MoreThanTwoOverlapVariantExistsException e)
  {
    super(e);
  }
}
