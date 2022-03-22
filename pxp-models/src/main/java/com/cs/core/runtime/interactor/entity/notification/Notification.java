package com.cs.core.runtime.interactor.entity.notification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Notification implements INotification {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          actedFor;
  protected String          actedBy;
  protected IEntityInfo     entityInfo;
  protected String          status;
  protected String          action;
  protected String          description;
  protected Long            createdOn;
  
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
  public String getActedFor()
  {
    return actedFor;
  }
  
  @Override
  public void setActedFor(String actedFor)
  {
    this.actedFor = actedFor;
  }
  
  @Override
  public String getActedBy()
  {
    return actedBy;
  }
  
  @Override
  public void setActedBy(String actedBy)
  {
    this.actedBy = actedBy;
  }
  
  @Override
  public IEntityInfo getEntityInfo()
  {
    return entityInfo;
  }
  
  @JsonDeserialize(as = EntityInfo.class)
  @Override
  public void setEntityInfo(IEntityInfo entityInfo)
  {
    this.entityInfo = entityInfo;
  }
  
  @Override
  public String getStatus()
  {
    return status;
  }
  
  @Override
  public void setStatus(String status)
  {
    this.status = status;
  }
  
  @Override
  public String getAction()
  {
    return action;
  }
  
  @Override
  public void setAction(String action)
  {
    this.action = action;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
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
}
