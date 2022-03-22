package com.cs.core.config.interactor.model.globalpermissions;

public class GetGlobalPermissionWithAllowedTemplatesRequestModel
    implements IGetGlobalPermissionWithAllowedTemplatesRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          permissionNodeId;
  protected String          natureKlassId;
  
  @Override
  public String getPermissionNodeId()
  {
    return permissionNodeId;
  }
  
  @Override
  public void setPermissionNodeId(String permissionNodeId)
  {
    this.permissionNodeId = permissionNodeId;
  }
  
  @Override
  public String getNatureKlassId()
  {
    return natureKlassId;
  }
  
  @Override
  public void setNatureKlassId(String natureKlassId)
  {
    this.natureKlassId = natureKlassId;
  }
}
