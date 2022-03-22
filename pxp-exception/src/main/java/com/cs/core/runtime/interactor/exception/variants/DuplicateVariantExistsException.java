package com.cs.core.runtime.interactor.exception.variants;

public class DuplicateVariantExistsException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  public DuplicateVariantExistsException()
  {
  }
  
  public DuplicateVariantExistsException(DuplicateVariantExistsException e)
  {
    super(e);
  }
}
