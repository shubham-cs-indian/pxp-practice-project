package com.cs.core.exception;


public class InvalidPhysicalCatalogIdsException extends Exception{
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public InvalidPhysicalCatalogIdsException()
  {
    super();
  }
  
  public InvalidPhysicalCatalogIdsException(Exception e)
  {
    super(e);
  }

  public InvalidPhysicalCatalogIdsException(String message)
  {
    super(message);
  }
  
}
