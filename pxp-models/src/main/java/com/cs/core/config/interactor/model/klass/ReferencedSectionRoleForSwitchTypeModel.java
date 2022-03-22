package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISectionRole;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedSectionRoleForSwitchTypeModel
    extends AbstractReferencedSectionElementForSwitchTypeModel implements ISectionRole {
  
  private static final long serialVersionUID = 1L;
  
  protected IRole           role;
  protected String          defaultValue     = "";
  
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
  @Override
  public IRole getRole()
  {
    return this.role;
  }
  
  @JsonDeserialize(as = Role.class)
  @Override
  public void setRole(IRole role)
  {
    this.role = role;
  }
}
