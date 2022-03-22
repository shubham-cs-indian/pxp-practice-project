package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

import com.cs.core.runtime.interactor.exception.onboarding.OnBoardingException;

public class EndPointException extends OnBoardingException {
  
  private static final long serialVersionUID = 1L;
  
  public EndPointException()
  {
  }
  
  public EndPointException(EndPointException endPointExceptions)
  {
    super();
  }
  
  public EndPointException(String message)
  {
    super(message);
  }
}
