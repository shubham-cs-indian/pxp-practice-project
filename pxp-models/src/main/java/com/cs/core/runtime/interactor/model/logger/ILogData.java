package com.cs.core.runtime.interactor.model.logger;

import java.io.Serializable;

public interface ILogData extends Serializable {
  
  public String getRequestMethod();
  
  public void setRequestMethod(String requestMethod);
  
  public String getUseCase();
  
  public void setUseCase(String useCase);
  
  public Long getStartTime();
  
  public void setStartTime(Long startTime);
  
  public Long getEndTime();
  
  public void setEndTime(Long endTime);
  
  public Long getTurnAroundTime();
  
  public String getExecutionStatus();
  
  public void setExecutionStatus(String executionStatus);
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public String getParentTransactionId();
  
  public void setParentTransactionId(String parentTransactionId);
}
