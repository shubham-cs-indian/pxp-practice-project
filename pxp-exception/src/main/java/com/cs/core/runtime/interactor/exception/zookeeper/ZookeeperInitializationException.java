package com.cs.core.runtime.interactor.exception.zookeeper;

import com.cs.core.exception.PluginException;

public class ZookeeperInitializationException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ZookeeperInitializationException()
  {
    super();
  }
  
  public ZookeeperInitializationException(String exceptionMessage)
  {
    super(exceptionMessage);
  }
  
  public ZookeeperInitializationException(Exception e)
  {
    super(e);
  }
}
