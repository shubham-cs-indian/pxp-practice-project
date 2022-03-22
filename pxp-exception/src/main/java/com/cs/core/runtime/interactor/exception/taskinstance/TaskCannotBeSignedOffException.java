package com.cs.core.runtime.interactor.exception.taskinstance;

import com.cs.core.exception.PluginException;

public class TaskCannotBeSignedOffException extends PluginException{
  
private static final long serialVersionUID = 1L;
  
  public TaskCannotBeSignedOffException()
  {
    super();
  }
  
  public TaskCannotBeSignedOffException(TaskCannotBeSignedOffException e)
  {
    super(e);
  }
}
