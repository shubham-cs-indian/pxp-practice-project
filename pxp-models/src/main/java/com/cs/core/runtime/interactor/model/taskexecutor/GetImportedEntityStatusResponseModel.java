package com.cs.core.runtime.interactor.model.taskexecutor;

import java.util.ArrayList;
import java.util.List;

public class GetImportedEntityStatusResponseModel implements IGetImportedEntityStatusResponseModel {
  
  private static final long serialVersionUID     = 1L;
  
  protected List<String>    successIds           = new ArrayList<>();
  protected List<String>    failedIds            = new ArrayList<>();
  protected Boolean         isComponentCompleted = false;
  protected Long            numberOfDocuments;
  
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
  public List<String> getFailedIds()
  {
    return failedIds;
  }
  
  @Override
  public void setFailedIds(List<String> failedIds)
  {
    this.failedIds = failedIds;
  }
  
  @Override
  public Boolean getIsComponentCompleted()
  {
    return isComponentCompleted;
  }
  
  @Override
  public void setIsComponentCompleted(Boolean isComponentCompleted)
  {
    this.isComponentCompleted = isComponentCompleted;
  }
  
  @Override
  public Long getNumberOfDocuments()
  {
    return numberOfDocuments;
  }
  
  @Override
  public void setNumberOfDocuments(Long numberOfDocuments)
  {
    this.numberOfDocuments = numberOfDocuments;
  }
}
