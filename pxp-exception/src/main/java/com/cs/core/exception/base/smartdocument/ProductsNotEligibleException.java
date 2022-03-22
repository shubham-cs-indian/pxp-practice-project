package com.cs.core.exception.base.smartdocument;

import com.cs.core.exception.PluginException;

public class ProductsNotEligibleException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ProductsNotEligibleException()
  {
  }
  
  public ProductsNotEligibleException(ProductsNotEligibleException e)
  {
    super(e);
  }
  
  
  public ProductsNotEligibleException(String message)
  {
    super(message);
  }
}
