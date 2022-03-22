package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class MappingsNotEmptyException extends MappingsException {
  
  private static final long serialVersionUID = 1L;
  
  public MappingsNotEmptyException()
  {
  }
  
  public MappingsNotEmptyException(MappingsNotEmptyException mappingsNotEmptyExceptions)
  {
    super();
  }
  
  public MappingsNotEmptyException(String message)
  {
    super(message);
  }
}
