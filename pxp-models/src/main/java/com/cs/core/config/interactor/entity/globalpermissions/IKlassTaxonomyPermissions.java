package com.cs.core.config.interactor.entity.globalpermissions;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IKlassTaxonomyPermissions extends IEntity {
  
  public static final String CAN_CREATE   = "canCreate";
  public static final String CAN_READ     = "canRead";
  public static final String CAN_EDIT     = "canEdit";
  public static final String CAN_DELETE   = "canDelete";
  public static final String ENTITY_ID    = "entityId";
  public static final String TYPE         = "type";
  public static final String CAN_DOWNLOAD = "canDownload";
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);
  
  public Boolean getCanRead();
  
  public void setCanRead(Boolean canRead);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getType();
  
  public void settype(String type);
  
  public Boolean getCanDownload();
  
  public void setCanDownload(Boolean canDownload);
}
