package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class KlassMappingsException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public KlassMappingsException()
  {
  }
  
  public KlassMappingsException(KlassMappingsException klassMappingsExceptions)
  {
    super();
  }
  
  public KlassMappingsException(String message)
  {
    super(message);
  }
}
