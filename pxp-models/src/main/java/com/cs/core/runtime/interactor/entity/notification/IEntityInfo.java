package com.cs.core.runtime.interactor.entity.notification;

import java.io.Serializable;

public interface IEntityInfo extends Serializable {
  
  public static final String ID               = "id";
  public static final String TYPE             = "type";
  public static final String SUB_ENTITY_ID    = "subEntityId";
  public static final String LABEL            = "label";
  public static final String SUB_ENTITY_LABEL = "subEntityLabel";
  
  public void setId(String id);
  
  public String getId();
  
  public void setType(String type);
  
  public String getType();
  
  public void setSubEntityId(String subEntityId);
  
  public String getSubEntityId();
  
  public void setLabel(String label);
  
  public String getLabel();
  
  public void setSubEntityLabel(String subEntityLabel);
  
  public String getSubEntityLabel();
}
