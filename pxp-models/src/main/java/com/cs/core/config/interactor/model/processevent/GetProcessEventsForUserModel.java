package com.cs.core.config.interactor.model.processevent;

public class GetProcessEventsForUserModel implements IGetProcessEventsForUserModel {
  
  private static final long serialVersionUID = 1L;
  protected String          userId;
  protected String          eventType;
  protected String          boardingType;
  
  @Override
  public String getEventType()
  {
    
    return eventType;
  }
  
  @Override
  public void setEventType(String eventType)
  {
    this.eventType = eventType;
  }
  
  @Override
  public String getUserId()
  {
    
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getBoardingType()
  {
    return boardingType;
  }
  
  @Override
  public void setBoardingType(String boardingType)
  {
    this.boardingType = boardingType;
  }
}
