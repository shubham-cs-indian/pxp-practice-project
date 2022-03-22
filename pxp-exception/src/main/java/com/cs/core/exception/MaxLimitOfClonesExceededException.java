package com.cs.core.exception;

public class MaxLimitOfClonesExceededException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public MaxLimitOfClonesExceededException()
  {
  }
  
  public MaxLimitOfClonesExceededException(Exception e)
  {
    super(e);
  }
}
