package com.cs.core.runtime.interactor.exception.onboarding.notfound;

public class FoundPreExistingEntitiesException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  public FoundPreExistingEntitiesException()
  {
  }
  
  public FoundPreExistingEntitiesException(FoundPreExistingEntitiesException columnNotFoundExceptions)
  {
    super();
  }
  
  public FoundPreExistingEntitiesException(String message)
  {
    super(message);
  }
}
