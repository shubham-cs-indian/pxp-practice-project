package com.cs.config.idto;

/**
 *  RelationshipPermission DTO from the configuration realm
 * @author mangesh.metkari
 *
 */
public interface IConfigRelationshipPermissionDTO extends IConfigJSONDTO {
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getCanAdd();
  
  public void setCanAdd(Boolean canAdd);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
}
