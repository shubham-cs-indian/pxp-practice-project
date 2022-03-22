package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdLabelWithGlobalPermissionModel extends IModel {
  
  public static final String ID                = "id";
  public static final String LABEL             = "label";
  public static final String IS_NATURE         = "isNature";
  public static final String GLOBAL_PERMISSION = "globalPermission";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String Label);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermissions);
}
