package com.cs.config.interactor.model.auditlog;

public class AuditLogModel implements IAuditLogModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Long            date;
  protected String          userName;
  protected String          ipAddress;
  protected String          activity;
  protected String          entityType;
  protected String          element;
  protected String          elementType;
  protected String          elementCode;
  protected String          elementName;
  protected String          activityId;
  
  @Override
  public String getActivityId()
  {
    return activityId;
  }
  
  @Override
  public void setActivityId(String activityId)
  {
    this.activityId = activityId;
  }
  
  @Override
  public Long getDate()
  {
    return date;
  }
  
  @Override
  public void setDate(Long date)
  {
    this.date = date;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getIpAddress()
  {
    return ipAddress;
  }
  
  @Override
  public void setIpAddress(String ipAddress)
  {
    this.ipAddress = ipAddress;
  }
  
  @Override
  public String getActivity()
  {
    return activity;
  }
  
  @Override
  public void setActivity(String activity)
  {
    this.activity = activity;
  }
  
  @Override
  public String getEntityType()
  {
    return entityType;
  }
  
  @Override
  public void setEntityType(String entityType)
  {
    this.entityType = entityType;
  }
  
  @Override
  public String getElement()
  {
    return element;
  }
  
  @Override
  public void setElement(String element)
  {
    this.element = element;
  }
  
  @Override
  public String getElementType()
  {
    return elementType;
  };
  
  @Override
  public void setElementType(String elementType)
  {
    this.elementType = elementType;
  }
  
  @Override
  public String getElementName()
  {
    return elementName;
  }
  
  @Override
  public void setElementName(String elementName)
  {
    this.elementName = elementName;
  }
  
  @Override
  public String getElementCode()
  {
    return elementCode;
  }
  
  @Override
  public void setElementCode(String elementCode)
  {
    this.elementCode = elementCode;
  }
  
}
