package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateOrSaveGlobalPermissionResponseModel extends ConfigResponseWithAuditLogModel
    implements ICreateOrSaveGlobalPermissionResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected List<IGetGlobalPermissionWithAllowedTemplatesModel> globalPermissionWithAllowedTemplates;
  protected IFunctionPermissionModel                            functionPermission;
  
  @Override
  public List<IGetGlobalPermissionWithAllowedTemplatesModel> getGlobalPermissionWithAllowedTemplates()
  {
    return globalPermissionWithAllowedTemplates;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetGlobalPermissionWithAllowedTemplatesModel.class)
  public void setGlobalPermissionWithAllowedTemplates(
      List<IGetGlobalPermissionWithAllowedTemplatesModel> globalPermissionWithAllowedTemplates)
  {
    this.globalPermissionWithAllowedTemplates = globalPermissionWithAllowedTemplates;
  }

  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }

  @Override
  @JsonDeserialize(as =  FunctionPermissionModel.class)
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
}
