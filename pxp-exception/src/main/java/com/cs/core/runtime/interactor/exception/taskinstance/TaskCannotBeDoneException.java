package com.cs.core.runtime.interactor.exception.taskinstance;

import com.cs.core.exception.PluginException;

public class TaskCannotBeDoneException extends PluginException{
  
private static final long serialVersionUID = 1L;
  
  public TaskCannotBeDoneException()
  {
    super();
  }
  
  public TaskCannotBeDoneException(TaskCannotBeDoneException e)
  {
    super(e);
  }
}
