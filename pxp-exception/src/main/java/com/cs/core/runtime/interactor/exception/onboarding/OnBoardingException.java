package com.cs.core.runtime.interactor.exception.onboarding;

import com.cs.core.exception.PluginException;

public class OnBoardingException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public OnBoardingException()
  {
  }
  
  public OnBoardingException(OnBoardingException onboardingException)
  {
    super(onboardingException);
  }
  
  public OnBoardingException(String message)
  {
    super(message);
  }
}
