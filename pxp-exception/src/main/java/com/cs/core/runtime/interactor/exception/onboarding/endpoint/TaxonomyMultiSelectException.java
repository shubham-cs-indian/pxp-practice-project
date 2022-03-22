package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class TaxonomyMultiSelectException extends MultiSelectException {
  
  private static final long serialVersionUID = 1L;
  
  public TaxonomyMultiSelectException()
  {
  }
  
  public TaxonomyMultiSelectException(TaxonomyMultiSelectException taxonomyMultiSelectExceptions)
  {
    super();
  }
  
  public TaxonomyMultiSelectException(String message)
  {
    super(message);
  }
}
