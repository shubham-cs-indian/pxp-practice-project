package com.cs.core.runtime.interactor.exception.onboarding.component;

import com.cs.core.runtime.interactor.exception.onboarding.OnBoardingException;

public class ComponentException extends OnBoardingException {
  
  private static final long serialVersionUID = 1L;
  
  public ComponentException()
  {
  }
  
  public ComponentException(ComponentException componentException)
  {
    super();
  }
  
  public ComponentException(String message)
  {
    super(message);
  }
}
