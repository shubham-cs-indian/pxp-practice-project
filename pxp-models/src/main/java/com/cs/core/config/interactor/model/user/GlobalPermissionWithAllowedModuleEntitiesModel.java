package com.cs.core.config.interactor.model.user;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionWithAllowedModuleEntitiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.Set;

public class GlobalPermissionWithAllowedModuleEntitiesModel
    implements IGlobalPermissionWithAllowedModuleEntitiesModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected Set<String>       allowedEntities  = new HashSet<>();
  protected IGlobalPermission globalPermission;
  
  @Override
  public Set<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(Set<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @Override
  @JsonDeserialize(as = GlobalPermission.class)
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
}
