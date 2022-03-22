package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class TagMultiSelectException extends MultiSelectException {
  
  private static final long serialVersionUID = 1L;
  
  public TagMultiSelectException()
  {
  }
  
  public TagMultiSelectException(TagMultiSelectException tagMultiSelectExceptions)
  {
    super();
  }
  
  public TagMultiSelectException(String message)
  {
    super(message);
  }
}
