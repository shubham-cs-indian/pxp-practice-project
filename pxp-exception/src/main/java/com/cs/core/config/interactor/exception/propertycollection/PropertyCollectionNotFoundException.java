package com.cs.core.config.interactor.exception.propertycollection;

import com.cs.core.exception.NotFoundException;

public class PropertyCollectionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public PropertyCollectionNotFoundException()
  {
  }
  
  public PropertyCollectionNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
