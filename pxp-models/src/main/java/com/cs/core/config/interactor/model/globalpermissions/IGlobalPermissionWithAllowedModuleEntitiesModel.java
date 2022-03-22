package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IGlobalPermissionWithAllowedModuleEntitiesModel extends IModel {
  
  public static final String GLOBAL_PERMISSION = "globalPermission";
  public static final String ALLOWED_ENTITIES  = "allowedEntities";
  
  public Set<String> getAllowedEntities();
  
  public void setAllowedEntities(Set<String> allowedEntities);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
}
