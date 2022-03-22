package com.cs.core.runtime.interactor.exception.processinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class ProcessInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public ProcessInstanceNotFoundException()
  {
    super();
  }
  
  public ProcessInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
