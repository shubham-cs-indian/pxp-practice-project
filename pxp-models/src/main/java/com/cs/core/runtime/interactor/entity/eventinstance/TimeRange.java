package com.cs.core.runtime.interactor.entity.eventinstance;

public class TimeRange implements ITimeRange {
  
  protected Long   startTime;
  protected Long   endTime;
  protected String id;
  protected Long   versionId;
  protected Long   versionTimestamp;
  protected String lastModifiedBy;
  
  @Override
  public Long getStartTime()
  {
    return startTime;
  }
  
  @Override
  public void setStartTime(Long startTime)
  {
    this.startTime = startTime;
  }
  
  @Override
  public Long getEndTime()
  {
    return endTime;
  }
  
  @Override
  public void setEndTime(Long endTime)
  {
    this.endTime = endTime;
  }
  
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
}
