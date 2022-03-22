package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.ColumnNotFoundException;

public class TaxonomyColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TaxonomyColumnNotFoundException()
  {
  }
  
  public TaxonomyColumnNotFoundException(
      TaxonomyColumnNotFoundException taxonomyColumnNotFoundExceptions)
  {
    super();
  }
  
  public TaxonomyColumnNotFoundException(String message)
  {
    super(message);
  }
}
