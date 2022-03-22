package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class MultiSelectException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public MultiSelectException()
  {
  }
  
  public MultiSelectException(MultiSelectException multiSelectExceptions)
  {
    super();
  }
  
  public MultiSelectException(String message)
  {
    super(message);
  }
}
