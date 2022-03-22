package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.Map;

public interface IKlassStructureViewSetting extends IConfigEntity {
  
  public Map<String, IKlassStructureRoleSetting> getRoles();
  
  public void setRoles(Map<String, IKlassStructureRoleSetting> roles);
}
