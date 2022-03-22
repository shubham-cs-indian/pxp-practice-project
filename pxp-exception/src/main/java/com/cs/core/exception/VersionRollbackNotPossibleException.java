package com.cs.core.exception;

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
