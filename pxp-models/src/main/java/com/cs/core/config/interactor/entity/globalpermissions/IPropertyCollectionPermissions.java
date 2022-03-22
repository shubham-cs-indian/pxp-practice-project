package com.cs.core.config.interactor.entity.globalpermissions;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IPropertyCollectionPermissions extends IEntity {
  
  public static final String IS_HIDDEN    = "isHidden";
  public static final String IS_COLLAPSED = "isCollapsed";
  public static final String CAN_EDIT     = "canEdit";
  public static final String ENTITY_ID    = "entityId";
  
  public Boolean getIsHidden();
  
  public void setIsHidden(Boolean isHidden);
  
  public Boolean getIsCollapsed();
  
  public void setIsCollapsed(Boolean isCollapsed);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
}
