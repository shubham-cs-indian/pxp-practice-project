package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class SupplierInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceNotFoundException()
  {
    super();
  }
  
  public SupplierInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
