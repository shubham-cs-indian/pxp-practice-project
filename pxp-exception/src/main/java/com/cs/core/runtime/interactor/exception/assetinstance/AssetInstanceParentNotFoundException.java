package com.cs.core.runtime.interactor.exception.assetinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.ParentNotFoundException;

public class AssetInstanceParentNotFoundException extends ParentNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetInstanceParentNotFoundException()
  {
    super();
  }
  
  public AssetInstanceParentNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
