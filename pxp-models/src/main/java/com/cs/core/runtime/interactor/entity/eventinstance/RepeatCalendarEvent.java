package com.cs.core.runtime.interactor.entity.eventinstance;

import java.util.Map;

public class RepeatCalendarEvent implements IRepeatCalendarEvent {
  
  protected String               repeatType;
  protected Map<String, Boolean> daysOfWeek;
  protected Long                 endsOn;
  protected Integer              endsAfter;
  protected Integer              repeatEvery;
  protected String               id;
  protected Long                 versionId;
  protected Long                 versionTimestamp;
  protected String               lastModifiedBy;
  
  @Override
  public String getRepeatType()
  {
    return repeatType;
  }
  
  @Override
  public void setRepeatType(String repeatType)
  {
    this.repeatType = repeatType;
  }
  
  @Override
  public Long getEndsOn()
  {
    return endsOn;
  }
  
  @Override
  public void setEndsOn(Long endsOn)
  {
    this.endsOn = endsOn;
  }
  
  @Override
  public Integer getRepeatEvery()
  {
    return repeatEvery;
  }
  
  @Override
  public void setRepeatEvery(Integer interval)
  {
    this.repeatEvery = interval;
  }
  
  @Override
  public Map<String, Boolean> getDaysOfWeek()
  {
    return daysOfWeek;
  }
  
  @Override
  public void setDaysOfWeek(Map<String, Boolean> daysOfWeek)
  {
    this.daysOfWeek = daysOfWeek;
  }
  
  @Override
  public Integer getEndsAfter()
  {
    return endsAfter;
  }
  
  @Override
  public void setEndsAfter(Integer endsAfter)
  {
    this.endsAfter = endsAfter;
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
