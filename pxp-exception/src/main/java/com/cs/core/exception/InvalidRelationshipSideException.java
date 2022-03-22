package com.cs.core.exception;

public class InvalidRelationshipSideException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public InvalidRelationshipSideException()
  {
    super();
  }
  
  public InvalidRelationshipSideException(Exception e)
  {
    super(e);
  }
}
