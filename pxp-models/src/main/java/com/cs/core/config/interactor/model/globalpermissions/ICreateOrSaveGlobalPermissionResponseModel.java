package com.cs.core.config.interactor.model.globalpermissions;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;

public interface ICreateOrSaveGlobalPermissionResponseModel
    extends IConfigResponseWithAuditLogModel {
  
  public static final String GLOBAL_PERMISSION_WITH_ALLOWED_TEMPLATES = "globalPermissionWithAllowedTemplates";
  public static final String FUNCTION_PERMISSION                      = "functionPermission";
  
  public List<IGetGlobalPermissionWithAllowedTemplatesModel> getGlobalPermissionWithAllowedTemplates();
  public void setGlobalPermissionWithAllowedTemplates(
      List<IGetGlobalPermissionWithAllowedTemplatesModel> globalPermissionWithAllowedTemplates);
  
  public IFunctionPermissionModel getFunctionPermission();
  
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
  
}
