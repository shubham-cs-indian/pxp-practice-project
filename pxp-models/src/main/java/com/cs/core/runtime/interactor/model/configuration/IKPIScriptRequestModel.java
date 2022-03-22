package com.cs.core.runtime.interactor.model.configuration;

public interface IKPIScriptRequestModel extends IModel{
  
  public static String SHOULD_EVALUATE_IDENTIFIER = "shouldEvaluateIdentifier";
  
  public boolean getShouldEvaluateIdentifier();
  public void setShouldEvaluateIdentifier(boolean shouldEvaluateIdentifier);
  
}
