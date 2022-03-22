package com.cs.core.config.interactor.model.permission;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPermissionWithHierarchyModel implements IGetPermissionWithHierarchyModel {
  
  private static final long  serialVersionUID = 1L;
  protected String           id;
  protected String           code;
  protected String           label;
  protected IPermissionModel permission;
  protected Boolean          isNature;
  protected String           type;
  protected String           natureType;
  
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
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public IPermissionModel getPermission()
  {
    return permission;
  }
  
  @Override
  @JsonDeserialize(as = PermissionModel.class)
  public void setPermission(IPermissionModel permission)
  {
    this.permission = permission;
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
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
  }
}
