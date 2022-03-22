package com.cs.core.runtime.interactor.entity.message;

public class MessageInformation implements IMessageInformation {
  
  private static final long serialVersionUID = 1L;
  protected int     notificationsCounter = 0;
  protected Boolean         isRed                   = false;
  protected Boolean         isOrange                = false;
  protected Boolean         isYellow                = false;
  protected Boolean         isGreen                 = false;
  protected Integer         redCount                = 0;
  protected Integer         orangeCount             = 0;
  protected Integer         yellowCount             = 0;
  protected String          validityMessage;
  protected Integer         shouldViolationCount    = 0;
  protected Integer         mandatoryViolationCount = 0;
  protected Integer         isUniqueViolationCount  = 0;
  
  @Override
  public int getNotificationsCounter()
  {
    return notificationsCounter;
  }
  
  @Override
  public void setNotificationsCounter(int notificationsCounter)
  {
    this.notificationsCounter = notificationsCounter;
  }
  
  @Override
  public Boolean getIsRed()
  {
    return isRed;
  }
  
  @Override
  public void setIsRed(Boolean isRed)
  {
    this.isRed = isRed;
  }
  
  @Override
  public Boolean getIsOrange()
  {
    return isOrange;
  }
  
  @Override
  public void setIsOrange(Boolean isOrange)
  {
    this.isOrange = isOrange;
  }
  
  @Override
  public Boolean getIsYellow()
  {
    return isYellow;
  }
  
  @Override
  public void setIsYellow(Boolean isYellow)
  {
    this.isYellow = isYellow;
  }
  
  @Override
  public Boolean getIsGreen()
  {
    return isGreen;
  }
  
  @Override
  public void setIsGreen(Boolean isGreen)
  {
    this.isGreen = isGreen;
  }
  
  @Override
  public Integer getRedCount()
  {
    return redCount;
  }
  
  @Override
  public void setRedCount(Integer redCount)
  {
    this.redCount = redCount;
  }
  
  @Override
  public Integer getOrangeCount()
  {
    return orangeCount;
  }
  
  @Override
  public void setOrangeCount(Integer orangeCount)
  {
    this.orangeCount = orangeCount;
  }
  
  @Override
  public Integer getYellowCount()
  {
    return yellowCount;
  }
  
  @Override
  public void setYellowCount(Integer yellowCount)
  {
    this.yellowCount = yellowCount;
  }
  
  @Override
  public String getValidityMessage()
  {
    return validityMessage;
  }
  
  @Override
  public void setValidityMessage(String validityMessage)
  {
    this.validityMessage = validityMessage;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Integer getShouldViolationCount()
  {
    return shouldViolationCount;
  }

  @Override
  public void setShouldViolationCount(Integer shouldViolationCount)
  {
    this.shouldViolationCount = shouldViolationCount;
  }

  @Override
  public Integer getMandatoryViolationCount()
  {
    return mandatoryViolationCount;
  }

  @Override
  public void setMandatoryViolationCount(Integer mandatoryViolationCount)
  {
    this.mandatoryViolationCount = mandatoryViolationCount;
  }

  @Override
  public Integer getIsUniqueViolationCount()
  {
    return isUniqueViolationCount;
  }

  @Override
  public void setIsUniqueViolationCount(Integer isUniqueViolationCount)
  {
    this.isUniqueViolationCount = isUniqueViolationCount;
  }
}
