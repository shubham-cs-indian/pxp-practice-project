package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class TextAssetInstanceAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceAlreadyExistsException()
  {
    super();
  }
  
  public TextAssetInstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
