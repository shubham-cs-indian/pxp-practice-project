package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class SupplierInstanceParentNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceParentNotFoundException()
  {
    super();
  }
  
  public SupplierInstanceParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
