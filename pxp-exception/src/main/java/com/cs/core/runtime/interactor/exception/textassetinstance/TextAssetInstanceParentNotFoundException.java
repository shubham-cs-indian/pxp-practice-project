package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class TextAssetInstanceParentNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceParentNotFoundException()
  {
    super();
  }
  
  public TextAssetInstanceParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
