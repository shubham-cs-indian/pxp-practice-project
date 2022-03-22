package com.cs.core.runtime.interactor.exception.onboarding.endpoint;

public class TaxonomyMappingsException extends EndPointException {
  
  private static final long serialVersionUID = 1L;
  
  public TaxonomyMappingsException()
  {
  }
  
  public TaxonomyMappingsException(TaxonomyMappingsException taxonomyMappingsExceptions)
  {
    super();
  }
  
  public TaxonomyMappingsException(String message)
  {
    super(message);
  }
}
