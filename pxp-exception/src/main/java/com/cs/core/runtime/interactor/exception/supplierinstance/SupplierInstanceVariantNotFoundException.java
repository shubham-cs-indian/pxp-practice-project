package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceVariantNotFoundException;

public class SupplierInstanceVariantNotFoundException extends InstanceVariantNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceVariantNotFoundException()
  {
    super();
  }
  
  public SupplierInstanceVariantNotFoundException(InstanceVariantNotFoundException e)
  {
    super(e);
  }
}
