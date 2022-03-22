package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.runtime.interactor.exception.configuration.InstanceAlreadyExistsException;

public class AssetInstanceAlreadyExistsException extends InstanceAlreadyExistsException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceAlreadyExistsException()
  {
    super();
  }
  
  public AssetInstanceAlreadyExistsException(InstanceAlreadyExistsException e)
  {
    super(e);
  }
}
