package com.cs.core.config.interactor.model.processevent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetProcessEventsForUserModel extends IModel {
  
  public static final String USER_ID       = "userId";
  public static final String EVENT_TYPE    = "eventType";
  public static final String BOARDING_TYPE = "boardingType";
  
  public String getBoardingType();
  
  public void setBoardingType(String boardingType);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getEventType();
  
  public void setEventType(String eventType);
}
