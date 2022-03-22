package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;

public interface IKlassGlobalPermissionModel extends IGlobalPermission {
  
  public static final String KLASS_ID = "klassId";
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
}
