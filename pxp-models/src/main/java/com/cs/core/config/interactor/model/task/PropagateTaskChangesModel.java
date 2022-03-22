package com.cs.core.config.interactor.model.task;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PropagateTaskChangesModel implements IPropagateTaskChangesModel {
  
  private static final long    serialVersionUID = 1L;
  
  protected String             id;
  protected String             newType;
  protected Map<String, IRole> referencedRoles;
  
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
  public String getNewType()
  {
    return newType;
  }
  
  @Override
  public void setNewType(String newType)
  {
    this.newType = newType;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
}
