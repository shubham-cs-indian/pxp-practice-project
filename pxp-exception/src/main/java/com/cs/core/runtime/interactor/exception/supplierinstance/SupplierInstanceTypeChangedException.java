package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.configuration.TypeChangedException;

public class SupplierInstanceTypeChangedException extends TypeChangedException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceTypeChangedException()
  {
    super();
  }
  
  public SupplierInstanceTypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
