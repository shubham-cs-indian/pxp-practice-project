package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.ColumnNotFoundException;

public class AddedKlassColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AddedKlassColumnNotFoundException()
  {
  }
  
  public AddedKlassColumnNotFoundException(
      AddedKlassColumnNotFoundException addedKlassColumnNotFoundExceptions)
  {
    super();
  }
  
  public AddedKlassColumnNotFoundException(String message)
  {
    super(message);
  }
}
