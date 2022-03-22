package com.cs.core.runtime.interactor.exception.onboarding.notfound;

import com.cs.core.runtime.interactor.exception.onboarding.component.ComponentException;

public class FoundMultipleEntitiesException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public FoundMultipleEntitiesException()
  {
  }
  
  public FoundMultipleEntitiesException(FoundMultipleEntitiesException columnNotFoundExceptions)
  {
    super();
  }
  
  public FoundMultipleEntitiesException(String message)
  {
    super(message);
  }
}
