package com.cs.core.runtime.interactor.entity.eventinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IEventInstanceNotification extends IEntity {
  
  public enum Type
  {
    NOTIFICATION, EMAIL
  }
  
  public void setType(Type type);
  
  public Type getType();
  
  public void setTriggerBefore(Long time);
  
  public Long getTriggerBefore();
  
  public void setMessage(String msg);
  
  public String getMessage();
}
