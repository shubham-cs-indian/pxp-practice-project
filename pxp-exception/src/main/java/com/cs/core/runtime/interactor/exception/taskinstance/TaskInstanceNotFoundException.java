package com.cs.core.runtime.interactor.exception.taskinstance;

import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.exception.configuration.InstanceNotFoundException;

public class TaskInstanceNotFoundException extends InstanceNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TaskInstanceNotFoundException()
  {
    super();
  }
  
  public TaskInstanceNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
