package com.cs.core.config.interactor.exception.klasssupplier;

import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;

public class SupplierKlassNotFoundException extends KlassNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierKlassNotFoundException()
  {
    super();
  }
  
  public SupplierKlassNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
