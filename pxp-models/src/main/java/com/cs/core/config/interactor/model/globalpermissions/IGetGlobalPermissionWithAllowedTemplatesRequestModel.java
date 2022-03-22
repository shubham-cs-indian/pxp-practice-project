package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetGlobalPermissionWithAllowedTemplatesRequestModel extends IModel {
  
  public static final String PERMISSION_NODE_ID = "permissionNodeId";
  public static final String NATURE_KLASS_ID    = "natureKlassId";
  
  public String getPermissionNodeId();
  
  public void setPermissionNodeId(String permissionNodeId);
  
  public String getNatureKlassId();
  
  public void setNatureKlassId(String natureKlassId);
}
