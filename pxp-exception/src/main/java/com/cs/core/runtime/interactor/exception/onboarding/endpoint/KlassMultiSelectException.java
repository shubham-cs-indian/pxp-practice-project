package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class KlassMultiSelectException extends MultiSelectException {
  
  private static final long serialVersionUID = 1L;
  
  public KlassMultiSelectException()
  {
  }
  
  public KlassMultiSelectException(KlassMultiSelectException klassMultiSelectExceptions)
  {
    super();
  }
  
  public KlassMultiSelectException(String message)
  {
    super(message);
  }
}
