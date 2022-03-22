package com.cs.core.config.interactor.model.governancerule;

import java.util.List;

public class AddedKPIRoleModel implements IAddedKPIRoleModel {
  
  private static final long serialVersionUID = 1L;
  protected String          roleId;
  protected List<String>    candidates;
  
  public String getRoleId()
  {
    return roleId;
  }
  
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  public List<String> getCandidates()
  {
    return candidates;
  }
  
  public void setCandidates(List<String> candidates)
  {
    this.candidates = candidates;
  }
}
