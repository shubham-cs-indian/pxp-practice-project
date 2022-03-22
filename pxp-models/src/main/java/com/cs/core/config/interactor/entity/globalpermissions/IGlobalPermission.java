package com.cs.core.config.interactor.entity.globalpermissions;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IGlobalPermission extends IConfigEntity {
  
  public static final String CAN_READ     = "canRead";
  public static final String CAN_EDIT     = "canEdit";
  public static final String CAN_DELETE   = "canDelete";
  public static final String CAN_CREATE   = "canCreate";
  public static final String CAN_DOWNLOAD = "canDownload";
  public static final String TYPE         = "type";
  public static final String ENTITY_ID    = "entityId";
  public static final String ROLE_ID      = "roleId";
  
  public Boolean getCanRead();
  
  public void setCanRead(Boolean canRead);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);
  
  public String getType();
  
  public void setType(String type);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public Boolean getCanDownload();
  
  public void setCanDownload(Boolean canDownload);
}
