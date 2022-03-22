package com.cs.core.runtime.interactor.entity.message;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IMessageInformation extends IEntity {
  
  public static final String NOTIFICATIONS_COUNTER = "notificationsCounter";
  public static final String IS_RED                = "isRed";
  public static final String IS_ORANGE             = "isOrange";
  public static final String IS_YELLOW             = "isYellow";
  public static final String RED_COUNT             = "redCount";
  public static final String ORANGE_COUNT          = "orangeCount";
  public static final String YELLOW_COUNT          = "yellowCount";
  public static final String IS_GREEN              = "isGreen";
  public static final String VALIDITY_MESSAGE      = "validityMessage";
  
  public int getNotificationsCounter();
  
  public void setNotificationsCounter(int notificationsCounter);
  
  public Boolean getIsRed();
  
  public void setIsRed(Boolean isRed);
  
  public Boolean getIsOrange();
  
  public void setIsOrange(Boolean isOrange);
  
  public Boolean getIsYellow();
  
  public void setIsYellow(Boolean isYellow);
  
  public Integer getRedCount();
  
  public void setRedCount(Integer redCount);
  
  public Integer getOrangeCount();
  
  public void setOrangeCount(Integer orangeCount);
  
  public Integer getYellowCount();
  
  public void setYellowCount(Integer yellowCount);
  
  public Boolean getIsGreen();
  
  public void setIsGreen(Boolean isGreen);
  
  public String getValidityMessage();
  
  public void setValidityMessage(String validityMessage);
  
  public Integer getShouldViolationCount();
  public void setShouldViolationCount(Integer shouldViolationCount);
  
  public Integer getMandatoryViolationCount();
  public void setMandatoryViolationCount(Integer mandatoryViolationCount);
  
  public Integer getIsUniqueViolationCount();
  public void setIsUniqueViolationCount(Integer isUniqueViolationCount);
}
