package com.cs.core.exception;


public class InvalidDashboardTabIdException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InvalidDashboardTabIdException()
  {
    super();
  }
  
  public InvalidDashboardTabIdException(Exception e)
  {
    super(e);
  }

  public InvalidDashboardTabIdException(String message)
  {
    super(message);
  }
  
}
