package com.cs.core.runtime.interactor.exception.camunda;

import com.cs.core.exception.PluginException;

public class WorkflowEngineException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public WorkflowEngineException(Exception e)
  {
    super(e);
  }
}
