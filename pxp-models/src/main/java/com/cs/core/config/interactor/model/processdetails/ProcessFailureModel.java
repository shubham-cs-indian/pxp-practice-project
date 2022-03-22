package com.cs.core.config.interactor.model.processdetails;

public class ProcessFailureModel extends ProcessStatusModel implements IProcessFailureModel {
  
  private static final long serialVersionUID = 1L;
  protected String          failureMessage;
  
  @Override
  public String getFailureMessage()
  {
    return failureMessage;
  }
  
  @Override
  public void setFailureMessage(String failureMessage)
  {
    this.failureMessage = failureMessage;
  }
}
