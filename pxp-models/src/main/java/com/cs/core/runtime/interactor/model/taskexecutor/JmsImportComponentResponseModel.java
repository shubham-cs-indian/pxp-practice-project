package com.cs.core.runtime.interactor.model.taskexecutor;

public class JmsImportComponentResponseModel implements IJmsImportComponentResponseModel {
  
  private static final long serialVersionUID        = 1L;
  
  protected Long            actualInstancesImported = 0l;
  protected String          failureMessage;
  
  @Override
  public Long getActualInstancesImported()
  {
    return actualInstancesImported;
  }
  
  @Override
  public void setActualInstancesImported(Long actualInstancesImported)
  {
    this.actualInstancesImported = actualInstancesImported;
  }
  
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
