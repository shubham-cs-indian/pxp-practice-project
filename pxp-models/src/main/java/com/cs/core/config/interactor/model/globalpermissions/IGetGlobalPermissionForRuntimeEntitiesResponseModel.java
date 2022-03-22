package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetGlobalPermissionForRuntimeEntitiesResponseModel extends IModel {
  
  public static final String GLOBAL_PERMISSIONS = "globalPermissions";
  
  public Map<String, IGlobalPermission> getGlobalPermissions();
  
  public void setGlobalPermissions(Map<String, IGlobalPermission> globalPermissions);
}
