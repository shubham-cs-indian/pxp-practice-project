package com.cs.core.config.interactor.entity.globalpermissions;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IPropertyPermissions extends IEntity {
  
  public static final String IS_DISABLED = "isDisabled";
  public static final String IS_HIDDEN   = "isHidden";
  public static final String CAN_EDIT    = "canEdit";
  public static final String ENTITY_ID   = "entityId";
  public static final String TYPE        = "type";
  
  public Boolean getIsDisabled();
  
  public void setIsDisabled(Boolean isDisabled);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void settype(String type);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getIsHidden();
  
  public void setIsHidden(Boolean isHidden);
}
