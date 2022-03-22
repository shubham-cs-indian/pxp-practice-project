package com.cs.core.runtime.interactor.entity.eventinstance;

public class EventInstanceNotification implements IEventInstanceNotification {
  
  protected Type   type;
  protected Long   triggerBefore;
  protected String message;
  protected String id;
  protected Long   versionId;
  protected Long   versionTimestamp;
  protected String lastModifiedBy;
  
  @Override
  public Type getType()
  {
    return type;
  }
  
  @Override
  public void setType(Type type)
  {
    this.type = type;
  }
  
  @Override
  public Long getTriggerBefore()
  {
    return triggerBefore;
  }
  
  @Override
  public void setTriggerBefore(Long triggerBefore)
  {
    this.triggerBefore = triggerBefore;
  }
  
  @Override
  public String getMessage()
  {
    return message;
  }
  
  @Override
  public void setMessage(String message)
  {
    this.message = message;
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
