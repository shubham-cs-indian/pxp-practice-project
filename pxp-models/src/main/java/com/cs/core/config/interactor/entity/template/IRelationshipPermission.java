package com.cs.core.config.interactor.entity.template;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IRelationshipPermission extends IEntity {
  
  public static final String IS_VISIBLE      = "isVisible";
  public static final String CAN_ADD         = "canAdd";
  public static final String CAN_DELETE      = "canDelete";
  public static final String ENTITY_ID       = "entityId";
  public static final String CODE            = "code";
  public static final String RELATIONSHIP_ID = "relationshipId";
  public static final String ROLE_ID         = "roleId";
  public static final String CAN_EDIT        = "canEdit";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getCanAdd();
  
  public void setCanAdd(Boolean canAdd);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
}
