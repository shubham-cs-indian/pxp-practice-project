package com.cs.core.runtime.interactor.exception.collections;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class StaticCollectionNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public StaticCollectionNotFoundException()
  {
    super();
  }
  
  public StaticCollectionNotFoundException(NotFoundException e)
  {
    super(e);
  }
  
  public StaticCollectionNotFoundException(InstanceNotFoundException e)
  {
    super(e);
  }
}
