package com.cs.core.config.interactor.exception.staticcollection;

import com.cs.core.exception.NotFoundException;

public class CollectionNodeNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public CollectionNodeNotFoundException()
  {
  }
  
  public CollectionNodeNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public CollectionNodeNotFoundException(String message, String errorCode)
  {
    super();
  }
}
