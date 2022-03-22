package com.cs.core.runtime.interactor.entity.role;

import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class RoleInstance extends PropertyInstance implements IRoleInstance {
  
  private static final long                serialVersionUID = 1L;
  
  protected List<? extends IRoleCandidate> candidates       = new ArrayList<>();
  protected String                         roleId;
  protected String                         baseType         = this.getClass()
      .getName();
  
  @Override
  public List<? extends IRoleCandidate> getCandidates()
  {
    return this.candidates;
  }
  
  @JsonDeserialize(contentAs = AbstractRoleCandidate.class)
  @Override
  public void setCandidates(List<? extends IRoleCandidate> candidates)
  {
    this.candidates = candidates;
  }
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String type)
  {
    this.baseType = type;
  }
}
