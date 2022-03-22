package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetGlobalPermissionForMultipleNatureTypesResponseModel extends IModel {
  
  public static final String GLOBAL_PERMISSION   = "globalPermission";
  public static final String FUNCTION_PERMISSION = "functionPermission";
  
  public Map<String, IGlobalPermission> getGlobalPermission();
  
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission);
  
  public IFunctionPermissionModel getFunctionPermission();
  
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
