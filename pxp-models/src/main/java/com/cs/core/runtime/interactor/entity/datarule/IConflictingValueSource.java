package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public interface IConflictingValueSource extends IEntity {
  
  public static final String ID   = "id";
  public static final String TYPE = "type";
  
  public String getType();
  
  public void setType(String type);
  
  public String getId();
  
  public void setId(String id);
}
