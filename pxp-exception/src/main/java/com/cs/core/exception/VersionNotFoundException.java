package com.cs.core.exception;

public class VersionNotFoundException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public VersionNotFoundException()
  {
  }
  
  public VersionNotFoundException(VersionNotFoundException e)
  {
    super(e);
  }
}
