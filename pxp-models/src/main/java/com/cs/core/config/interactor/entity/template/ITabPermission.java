package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface ITabPermission extends IConfigEntity {
  
  public static final String TYPE       = "type";
  public static final String IS_VISIBLE = "isVisible";
  public static final String CAN_CREATE = "canCreate";
  public static final String CAN_EDIT   = "canEdit";
  public static final String CAN_DELETE = "canDelete";
  public static final String ENTITY_ID  = "entityId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
}
