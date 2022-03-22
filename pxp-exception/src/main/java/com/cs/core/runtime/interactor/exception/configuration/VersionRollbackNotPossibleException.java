package com.cs.core.runtime.interactor.exception.configuration;

import com.cs.core.exception.PluginException;

public class VersionRollbackNotPossibleException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public VersionRollbackNotPossibleException()
  {
  }
  
  public VersionRollbackNotPossibleException(VersionRollbackNotPossibleException e)
  {
    super(e);
  }
  
  public VersionRollbackNotPossibleException(String message)
  {
    super(message);
  }
}
