package com.cs.core.runtime.interactor.exception.onboarding.column;

import com.cs.core.runtime.interactor.exception.onboarding.notfound.AssetComponentColumnNotFoundException;

public class FolderPathColumnNotFoundException extends AssetComponentColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public FolderPathColumnNotFoundException()
  {
  }
  
  public FolderPathColumnNotFoundException(
      FolderPathColumnNotFoundException folderPathColumnNotFoundExceptions)
  {
    super();
  }
  
  public FolderPathColumnNotFoundException(String message)
  {
    super(message);
  }
}
