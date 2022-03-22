package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveGlobalPermissionModel implements ISaveGlobalPermissionModel {
  
  private static final long                          serialVersionUID = 1L;
  protected List<ICreateOrSaveGlobalPermissionModel> list;
  protected IFunctionPermissionModel                 functionPermission;
  protected String                                   roleId;
  
  @Override
  public List<ICreateOrSaveGlobalPermissionModel> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = CreateOrSaveGlobalPermissionModel.class)
  @Override
  public void setList(List<ICreateOrSaveGlobalPermissionModel> list)
  {
    this.list = list;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }
  
  @JsonDeserialize(as = FunctionPermissionModel.class)
  @Override
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
  
  @Override
  public String getRoleId()
  {
    return this.roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
}

