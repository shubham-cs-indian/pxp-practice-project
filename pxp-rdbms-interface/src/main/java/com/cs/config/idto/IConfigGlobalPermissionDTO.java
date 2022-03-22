package com.cs.config.idto;


/**
 *  GlobalPermission DTO from the configuration realm
 * @author mangesh.metkari
 * 
 */
public interface IConfigGlobalPermissionDTO  extends IConfigJSONDTO{
  
  public Boolean getCanRead();
  
  public void setCanRead(Boolean canRead);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);

  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public Boolean getCanDownload();
  
  public void setCanDownload(Boolean canDownload);
  
  
  
}
