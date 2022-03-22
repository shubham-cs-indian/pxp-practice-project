package com.cs.core.runtime.interactor.exception.zookeeper;

import com.cs.core.exception.PluginException;

public class InstanceLockException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InstanceLockException()
  {
    super();
  }
  
  public InstanceLockException(String exceptionMessage)
  {
    super(exceptionMessage);
  }
  
  public InstanceLockException(Exception e)
  {
    super(e);
  }
}
