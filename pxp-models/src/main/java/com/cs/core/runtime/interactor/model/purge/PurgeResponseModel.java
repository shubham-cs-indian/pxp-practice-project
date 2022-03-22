package com.cs.core.runtime.interactor.model.purge;

import java.util.List;

public class PurgeResponseModel implements IPurgeResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    successIds;
  private List<String>      failureIds;
  
  @Override
  public List<String> getSuccessIds()
  {
    return successIds;
  }
  
  @Override
  public void setSuccessIds(List<String> successIds)
  {
    this.successIds = successIds;
  }
  
  @Override
  public List<String> getFailureIds()
  {
    return failureIds;
  }
  
  @Override
  public void setFailureIds(List<String> failureIds)
  {
    this.failureIds = failureIds;
  }
}
