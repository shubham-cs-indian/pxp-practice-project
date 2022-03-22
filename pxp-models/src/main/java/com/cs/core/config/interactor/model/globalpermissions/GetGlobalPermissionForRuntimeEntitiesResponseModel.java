package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetGlobalPermissionForRuntimeEntitiesResponseModel
    implements IGetGlobalPermissionForRuntimeEntitiesResponseModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected Map<String, IGlobalPermission> globalPermissions;
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermissions()
  {
    return globalPermissions;
  }
  
  @JsonDeserialize(contentAs = GlobalPermission.class)
  @Override
  public void setGlobalPermissions(Map<String, IGlobalPermission> globalPermissions)
  {
    this.globalPermissions = globalPermissions;
  }
}
