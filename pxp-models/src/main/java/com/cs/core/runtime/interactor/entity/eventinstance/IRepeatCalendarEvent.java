package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import java.util.Map;

public interface IRepeatCalendarEvent extends IEntity {
  
  public static final String REPEAT_TYPE  = "repeatType";
  public static final String DAYS         = "days";
  public static final String ENDS_ON      = "endsOn";
  public static final String REPEAT_EVERY = "repeatEvery";
  public static final String DAYS_OF_WEEK = "daysOfWeek";
  public static final String ENDS_AFTER   = "endsAfter";
  
  public String getRepeatType();
  
  public void setRepeatType(String repeatType);
  
  public void setEndsOn(Long endsOn);
  
  public Long getEndsOn();
  
  public void setRepeatEvery(Integer interval);
  
  public Integer getRepeatEvery();
  
  public void setDaysOfWeek(Map<String, Boolean> daysOfWeek);
  
  public Map<String, Boolean> getDaysOfWeek();
  
  public Integer getEndsAfter();
  
  public void setEndsAfter(Integer endsAfter);
}
