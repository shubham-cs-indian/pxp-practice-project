package com.cs.core.runtime.interactor.model.configuration;


public class KPIScriptRequestModel implements IKPIScriptRequestModel {
 
  private static final long serialVersionUID = 1L;
  boolean shouldEvaluateIdentifier = false;

  @Override
  public boolean getShouldEvaluateIdentifier()
  {
    return shouldEvaluateIdentifier;
  }

  @Override
  public void setShouldEvaluateIdentifier(boolean shouldEvaluateIdentifier)
  {
    this.shouldEvaluateIdentifier = shouldEvaluateIdentifier;
  }
  
  
}
