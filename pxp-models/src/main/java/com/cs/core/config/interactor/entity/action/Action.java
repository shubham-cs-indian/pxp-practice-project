package com.cs.core.config.interactor.entity.action;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Action implements IAction {
  
  protected String      id;
  protected Long        versionId;
  protected Long        versionTimestamp;
  protected String      lastModifiedBy;
  protected IParameters parameters;
  protected String      taskId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public IParameters getParameters()
  {
    return parameters;
  }
  
  @JsonDeserialize(as = Parameters.class)
  @Override
  public void setParameters(IParameters parameters)
  {
    this.parameters = parameters;
  }
  
  @Override
  public String getTaskId()
  {
    return taskId;
  }
  
  @Override
  public void setTaskId(String taskId)
  {
    this.taskId = taskId;
  }
}
