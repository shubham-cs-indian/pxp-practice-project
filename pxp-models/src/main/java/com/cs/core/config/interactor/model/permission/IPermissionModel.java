package com.cs.core.config.interactor.model.permission;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IPermissionModel extends IModel {
  
  public static final String CAN_READ     = "canRead";
  public static final String CAN_EDIT     = "canEdit";
  public static final String CAN_DELETE   = "canDelete";
  public static final String CAN_CREATE   = "canCreate";
  public static final String CAN_DOWNLOAD = "canDownload";
  
  public Boolean getCanRead();
  
  public void setCanRead(Boolean canRead);
  
  public Boolean getCanEdit();
  
  public void setCanEdit(Boolean canEdit);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
  
  public Boolean getCanCreate();
  
  public void setCanCreate(Boolean canCreate);
  
  public Boolean getCanDownload();
  
  public void setCanDownload(Boolean canDownload);
}
