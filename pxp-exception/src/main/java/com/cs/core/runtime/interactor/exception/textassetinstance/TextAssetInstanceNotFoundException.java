package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class TextAssetInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceNotFoundException()
  {
    super();
  }
  
  public TextAssetInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
