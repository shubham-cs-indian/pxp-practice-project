package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.role.IRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReferencedSectionRoleModel extends AbstractReferencedSectionElementModel
    implements IReferencedSectionRoleModel {
  
  private static final long serialVersionUID = 1L;
  protected String          defaultValue     = "";
  
  @Override
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
  /**
   * ************************* ignored properties
   * ********************************
   */
  @JsonIgnore
  @Override
  public IRole getRole()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setRole(IRole role)
  {
    // TODO Auto-generated method stub
    
  }
}
