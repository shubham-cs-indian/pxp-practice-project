package com.cs.core.runtime.interactor.exception.onboarding.component;

public class ComponentNotFoundException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public ComponentNotFoundException()
  {
  }
  
  public ComponentNotFoundException(ComponentNotFoundException componentNotFoundExceptions)
  {
    super();
  }
  
  public ComponentNotFoundException(String message)
  {
    super(message);
  }
}
