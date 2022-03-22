package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class TagMappingsException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public TagMappingsException()
  {
  }
  
  public TagMappingsException(TagMappingsException tagMappingsExceptions)
  {
    super();
  }
  
  public TagMappingsException(String message)
  {
    super(message);
  }
}
