package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class ProcessEndPointNotFoundException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public ProcessEndPointNotFoundException()
  {
  }
  
  public ProcessEndPointNotFoundException(
      ProcessEndPointNotFoundException processEndPointNotFoundExceptions)
  {
    super();
  }
  
  public ProcessEndPointNotFoundException(String message)
  {
    super(message);
  }
}
