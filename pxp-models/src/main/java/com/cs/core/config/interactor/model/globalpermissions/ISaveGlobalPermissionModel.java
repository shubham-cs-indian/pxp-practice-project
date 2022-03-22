package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveGlobalPermissionModel extends IModel {
  
  public static final String LIST                = "list";
  public static final String FUNCTION_PERMISSION = "functionPermission";
  public static final String ROLE_ID             = "roleId";
  
  public List<ICreateOrSaveGlobalPermissionModel> getList();
  public void setList(List<ICreateOrSaveGlobalPermissionModel> list);
  
  public IFunctionPermissionModel getFunctionPermission();
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);

  public String getRoleId();
  public void setRoleId(String roleId);
}

