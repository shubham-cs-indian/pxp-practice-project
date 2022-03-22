package com.cs.core.runtime.interactor.exception.supplierinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class SupplierInstanceAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierInstanceAlreadyExistsException()
  {
    super();
  }
  
  public SupplierInstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
