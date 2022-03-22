package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.AssetComponentColumnNotFoundException;

public class FilePathColumnNotFoundException extends AssetComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public FilePathColumnNotFoundException()
  {
  }
  
  public FilePathColumnNotFoundException(
      FilePathColumnNotFoundException filePathColumnNotFoundExceptions)
  {
    super();
  }
  
  public FilePathColumnNotFoundException(String message)
  {
    super(message);
  }
}
