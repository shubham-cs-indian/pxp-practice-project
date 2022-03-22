package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IEventInstanceSchedule extends IEntity {
  
  public static final String START_TIME = "startTime";
  public static final String END_TIME   = "endTime";
  public static final String REPEAT     = "repeat";
  
  public void setStartTime(Long startTime);
  
  public Long getStartTime();
  
  public void setEndTime(Long endTime);
  
  public Long getEndTime();
  
  public void setRepeat(IRepeatCalendarEvent repeat);
  
  public IRepeatCalendarEvent getRepeat();
}
