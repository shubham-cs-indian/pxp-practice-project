package com.cs.config.idto;

/**
 * PropertyPermission DTO from the configuration realm
 * @author mangesh.metkari
 *
 */
public interface IConfigPropertyPermissionDTO extends IConfigJSONDTO {
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
  
  public Boolean getIsVisible();
  
  public void setIsVisible(Boolean isVisible);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public String getPropertyType();
  
  public void setPropertyType(String propertyType);
  
}
