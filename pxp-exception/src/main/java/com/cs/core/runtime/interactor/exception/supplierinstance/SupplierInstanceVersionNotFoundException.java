package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.configuration.VersionNotFoundException;

public class SupplierInstanceVersionNotFoundException extends VersionNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceVersionNotFoundException()
  {
    super();
  }
  
  public SupplierInstanceVersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
