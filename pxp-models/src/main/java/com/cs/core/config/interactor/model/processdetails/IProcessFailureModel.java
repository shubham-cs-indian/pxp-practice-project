package com.cs.core.config.interactor.model.processdetails;

public interface IProcessFailureModel extends IProcessStatusModel {
  
  public static final String FAILURE_MESSAGE = "failureMessage";
  
  public String getFailureMessage();
  
  public void setFailureMessage(String failureMessage);
}
