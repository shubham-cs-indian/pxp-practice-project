package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.VersionNotFoundException;

public class TextAssetInstanceVersionNotFoundException extends VersionNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceVersionNotFoundException()
  {
    super();
  }
  
  public TextAssetInstanceVersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
