package com.cs.core.runtime.interactor.exception.onboarding.notfound;

import com.cs.core.runtime.interactor.exception.onboarding.component.ComponentException;

public class LabelNotFoundException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public LabelNotFoundException()
  {
  }
  
  public LabelNotFoundException(LabelNotFoundException columnNotFoundExceptions)
  {
    super();
  }
  
  public LabelNotFoundException(String message)
  {
    super(message);
  }
}
