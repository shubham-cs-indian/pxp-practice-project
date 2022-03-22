package com.cs.core.config.interactor.exception.task;

import com.cs.core.exception.NotFoundException;

public class TaskNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TaskNotFoundException()
  {
  }
  
  public TaskNotFoundException(String message, String errorCode)
  {
    super();
  }
}
