package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class MappingsException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public MappingsException()
  {
  }
  
  public MappingsException(MappingsException mappingsExceptions)
  {
    super();
  }
  
  public MappingsException(String message)
  {
    super(message);
  }
}
