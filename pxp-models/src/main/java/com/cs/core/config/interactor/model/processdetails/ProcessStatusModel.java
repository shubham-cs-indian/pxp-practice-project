package com.cs.core.config.interactor.model.processdetails;

public class ProcessStatusModel extends ProcessModel implements IProcessStatusModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         status;
  
  @Override
  public Boolean getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(Boolean status)
  {
    this.status = status;
  }
}
