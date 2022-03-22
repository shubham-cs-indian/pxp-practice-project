package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.role.IRolePermission;

import java.util.Map;

public interface IKlassPermission extends IConfigEntity {
  
  public static final String ROLE_PERMISSION = "rolePermission";
  
  public Map<String, IRolePermission> getRolePermission();
  
  public void setRolePermission(Map<String, IRolePermission> rolePermission);
}
