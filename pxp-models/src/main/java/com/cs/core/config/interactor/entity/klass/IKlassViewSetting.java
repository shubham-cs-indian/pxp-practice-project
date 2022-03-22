package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface IKlassViewSetting extends IConfigEntity {
  
  public Map<String, IKlassRoleSetting> getRoles();
  
  public void setRoles(Map<String, IKlassRoleSetting> roles);
}
