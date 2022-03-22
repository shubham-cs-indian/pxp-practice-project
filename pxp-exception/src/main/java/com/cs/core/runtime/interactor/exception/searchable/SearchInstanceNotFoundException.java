package com.cs.core.runtime.interactor.exception.searchable;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class SearchInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public SearchInstanceNotFoundException()
  {
    super();
  }
  
  public SearchInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
