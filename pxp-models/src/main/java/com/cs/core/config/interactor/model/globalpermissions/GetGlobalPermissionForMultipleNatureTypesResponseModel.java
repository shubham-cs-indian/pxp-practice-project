package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetGlobalPermissionForMultipleNatureTypesResponseModel
    implements IGetGlobalPermissionForMultipleNatureTypesResponseModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected Map<String, IGlobalPermission> globalPermission;
  protected IFunctionPermissionModel       functionPermission;
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    return globalPermission;
  }
  
  @Override
  @JsonDeserialize(contentAs = GlobalPermission.class)
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }
  
  @Override
  @JsonDeserialize(as = FunctionPermissionModel.class)
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
}
