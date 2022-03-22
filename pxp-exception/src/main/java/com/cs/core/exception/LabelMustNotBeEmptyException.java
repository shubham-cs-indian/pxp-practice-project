package com.cs.core.exception;


public class LabelMustNotBeEmptyException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public LabelMustNotBeEmptyException()
  {
    super();
  }
  
  public LabelMustNotBeEmptyException(Exception e)
  {
    super(e);
  }

  public LabelMustNotBeEmptyException(String message)
  {
    super(message);
  }
  
}
