package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.model.user.GlobalPermissionWithAllowedModuleEntitiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetGlobalPermissionForMultipleEntityResponseModel
    implements IGetGlobalPermissionForMultipleEntityResponseModel {
  
  private static final long                                              serialVersionUID = 1L;
  
  protected Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> globalPermission;
  
  @Override
  public Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(contentAs = GlobalPermissionWithAllowedModuleEntitiesModel.class)
  @Override
  public void setGlobalPermission(
      Map<String, IGlobalPermissionWithAllowedModuleEntitiesModel> globalPermission)
  {
    this.globalPermission = globalPermission;
  }
}
