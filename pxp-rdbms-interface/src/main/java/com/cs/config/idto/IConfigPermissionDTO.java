package com.cs.config.idto;

import java.util.List;

/**
 *  Permission DTO from the configuration realm
 * @author mangesh.metkari
 *
 */

public interface IConfigPermissionDTO extends IConfigJSONDTO {
  
 public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getPermissionType();
  
  public void setPermissionType(String permissionType);
  
  public String getRoleId();
  
  public void setRoleId(String roleId); 
  
  public IConfigGlobalPermissionDTO getGlobalPermission();
  
  public IConfigHeaderPermisionDTO getHeaderPermission();
  
  public List<IConfigPropertyPermissionDTO> getPropertyPermission();
  
  public List<IConfigRelationshipPermissionDTO> getRelationshipPermission();
  
}
