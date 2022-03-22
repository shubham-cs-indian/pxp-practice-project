package com.cs.core.config.interactor.entity.condition;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IConditionEntity extends IEntity {
  
  public String getType();
  
  public void setType(String type);
  
  public String getListId();
  
  public void setListId(String listId);
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getBaseType();
  
  public void setBaseType(String type);
}
