package com.cs.core.runtime.interactor.entity.notification;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface INotification extends IEntity {
  
  public static final String ACTED_FOR   = "actedFor";
  public static final String ACTED_BY    = "actedBy";
  public static final String ENTITY_INFO = "entityInfo";
  public static final String STATUS      = "status";
  public static final String ACTION      = "action";
  public static final String DESCRIPTION = "description";
  public static final String CREATED_ON  = "createdOn";
  
  public String getId();
  
  public void setId(String id);
  
  public String getActedFor();
  
  public void setActedFor(String actedFor);
  
  public String getActedBy();
  
  public void setActedBy(String actedBy);
  
  public IEntityInfo getEntityInfo();
  
  public void setEntityInfo(IEntityInfo entityInfo);
  
  public String getAction();
  
  public void setAction(String action);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getStatus();
  
  public void setStatus(String status);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
}
