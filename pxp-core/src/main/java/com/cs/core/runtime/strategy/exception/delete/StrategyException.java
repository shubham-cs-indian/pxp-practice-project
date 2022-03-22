package com.cs.core.runtime.strategy.exception.delete;

public class StrategyException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  protected Object          responseModel;
  
  public StrategyException(Object responseModel)
  {
    this.responseModel = responseModel;
  }
  
  public Object getResponseModel()
  {
    return responseModel;
  }
  
  public void setResponseModel(Object responseModel)
  {
    this.responseModel = responseModel;
  }
}
