package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class IdLabelWithGlobalPermissionModel implements IIdLabelWithGlobalPermissionModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected IGlobalPermission globalPermission;
  protected String            id;
  protected String            label;
  protected Boolean           isNature;
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
}
