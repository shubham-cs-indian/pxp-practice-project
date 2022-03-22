package com.cs.core.runtime.interactor.exception.onboarding.notfound;

import com.cs.core.runtime.interactor.exception.onboarding.component.ComponentException;

public class SheetNotFoundException extends ComponentException {
  
  private static final long serialVersionUID = 1L;
  
  public SheetNotFoundException()
  {
    super();
  }
  
  public SheetNotFoundException(SheetNotFoundException sheetNotFoundException)
  {
    super();
  }
  
  public SheetNotFoundException(String message)
  {
    super(message);
  }
}
