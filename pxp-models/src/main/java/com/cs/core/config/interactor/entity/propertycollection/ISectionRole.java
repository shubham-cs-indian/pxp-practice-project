package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.role.IRole;

public interface ISectionRole extends ISectionElement {
  
  public static final String ROLE          = "role";
  public static final String DEFAULT_VALUE = "defaultValue";
  
  public String getDefaultValue();
  
  public void setDefaultValue(String defaultValue);
  
  public IRole getRole();
  
  public void setRole(IRole role);
}
