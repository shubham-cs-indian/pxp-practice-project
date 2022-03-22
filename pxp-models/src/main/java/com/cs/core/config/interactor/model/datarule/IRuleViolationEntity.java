package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IRuleViolationEntity extends IEntity {
  
  public static final String ID          = "id";
  public static final String ENTITY_ID   = "entityId";
  public static final String TYPE        = "type";
  public static final String DESCRIPTION = "description";
  public static final String COLOR       = "color";
  public static final String CODE        = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public String getDescription();
  
  public void setDescription(String message);
  
  public String getColor();
  
  public void setColor(String color);
}
