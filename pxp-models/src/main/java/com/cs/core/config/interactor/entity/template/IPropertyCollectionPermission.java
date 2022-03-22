package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IPropertyCollectionPermission extends IEntity {
  
  public static final String IS_VISIBLE             = "isVisible";
  public static final String IS_EXPANDED            = "isExpanded";
  public static final String CAN_EDIT               = "canEdit";
  public static final String ENTITY_ID              = "entityId";
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  public static final String ROLE_ID                = "roleId";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getPropertyCollectionId();
  
  public void setPropertyCollectionId(String propertyCollectionId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getIsExpanded();
  
  public void setIsExpanded(Boolean isExpanded);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
}
