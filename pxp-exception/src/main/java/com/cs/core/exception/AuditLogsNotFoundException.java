package com.cs.core.exception;


public class AuditLogsNotFoundException extends NotFoundException {
 
  private static final long serialVersionUID = 1L;
  
  public AuditLogsNotFoundException()
  {
  }
  
  public AuditLogsNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
