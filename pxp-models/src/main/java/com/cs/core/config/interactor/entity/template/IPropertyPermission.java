package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IPropertyPermission extends IEntity {
  
  public static final String IS_VISIBLE  = "isVisible";
  public static final String CAN_EDIT    = "canEdit";
  public static final String ENTITY_ID   = "entityId";
  public static final String TYPE        = "type";
  public static final String PROPERTY_ID = "propertyId";
  public static final String ROLE_ID     = "roleId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public String getType();
  
  public void setType(String type);
}
