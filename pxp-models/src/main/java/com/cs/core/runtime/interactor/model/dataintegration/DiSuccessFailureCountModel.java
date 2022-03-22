package com.cs.core.runtime.interactor.model.dataintegration;

public class DiSuccessFailureCountModel implements IDiSuccessFailureCountModel {
  
  private static final long serialVersionUID = 1L;
  public String             successCount;
  public String             failureCount;
  
  public String getSuccessCount()
  {
    return successCount;
  }
  
  public void setSuccessCount(String successCount)
  {
    this.successCount = successCount;
  }
  
  public String getFailureCount()
  {
    return failureCount;
  }
  
  public void setFailureCount(String failureCount)
  {
    this.failureCount = failureCount;
  }
}
