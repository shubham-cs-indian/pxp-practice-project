package com.cs.core.runtime.interactor.model.notification;

import com.cs.core.runtime.interactor.entity.notification.EntityInfo;
import com.cs.core.runtime.interactor.entity.notification.IEntityInfo;
import com.cs.core.runtime.interactor.entity.notification.INotification;
import com.cs.core.runtime.interactor.entity.notification.Notification;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class NotificationModel implements INotificationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected INotification   entity;
  
  public NotificationModel()
  {
    entity = new Notification();
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getActedFor()
  {
    return entity.getActedFor();
  }
  
  @Override
  public void setActedFor(String actedFor)
  {
    entity.setActedFor(actedFor);
  }
  
  @Override
  public String getActedBy()
  {
    return entity.getActedBy();
  }
  
  @Override
  public void setActedBy(String actedBy)
  {
    entity.setActedBy(actedBy);
  }
  
  @Override
  public IEntityInfo getEntityInfo()
  {
    return entity.getEntityInfo();
  }
  
  @JsonDeserialize(as = EntityInfo.class)
  @Override
  public void setEntityInfo(IEntityInfo entityInfo)
  {
    entity.setEntityInfo(entityInfo);
  }
  
  @Override
  public String getAction()
  {
    return entity.getAction();
  }
  
  @Override
  public void setAction(String action)
  {
    entity.setAction(action);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public String getStatus()
  {
    return entity.getStatus();
  }
  
  @Override
  public void setStatus(String status)
  {
    entity.setStatus(status);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
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
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
