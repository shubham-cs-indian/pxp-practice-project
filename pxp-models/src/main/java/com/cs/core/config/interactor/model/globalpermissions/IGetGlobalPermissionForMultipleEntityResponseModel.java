package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetGlobalPermissionForMultipleEntityResponseModel extends IModel {
  
  public static final String GLOBAL_PERMISSION = "globalPermission";
  
  public Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> getGlobalPermission();
  
  public void setGlobalPermission(
      Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> globalPermission);
}
